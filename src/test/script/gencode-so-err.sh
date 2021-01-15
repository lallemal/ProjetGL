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
cd "$(dirname "$0")"/../../.. || exit 1
