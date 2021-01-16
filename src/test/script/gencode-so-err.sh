#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

cd src/test/deca/codegen/invalid/sansObjetPourScript || exit 1
rm -f ./*.ass 2>/dev/null
nb=$(ls -l *.deca | wc -l)
echo "------ Démarrage des tests ($nb)"
for i in *.deca
do
    decac ./$i || exit 1
done
#on met a part le test de debordement de pile car il necessite une execution de ima -p nnn
decac ./debordementPile/pilePleine.deca || exit 1

for i in *.ass
do
    resultat=$(ima ./$i)
    if [ "$(cat Modeles_OK/$i.ok)" = "$resultat" ]; then
        echo "$i OK"
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat Modeles_OK/$i.ok)"
        exit 1
  fi
done

#on met a part le test de debordement de pile car il necessite une execution de ima -p nnn
resultat=$(ima -p 003 ./debordementPile/pilePleine.ass)
if [ "$(cat ./debordementPile/Modeles_OK/pilePleine.ass.ok)" = "$resultat" ]; then
    echo "pilePeine.ass OK"
else
    echo "pilePeine.ass KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat ./debordementPile/Modeles_OK/pilePleine.ass.ok)"
    exit 1
fi

cd ../../../../../.. || exit 1

#Test sur un include

decac ./src/test/deca/decompile/Test_include/Include.deca || exit 1
resultat=$(ima ./src/test/deca/decompile/Test_include/Include.ass)

    if [ "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Modeles_OK/Include.ass.ok)" = "$resultat" ]; then
        echo "Include.ass OK"
    else
        echo "Include.ass KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Modeles_OK/Include.ass.ok)"
        exit 1
  fi

cd "$(dirname "$0")"/../../.. || exit 1
