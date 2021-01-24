#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la bibliothèque de code en sans objet FICHIERS VALIDES.
# On génere les .ass puis on compare les .ass obtenus à celle voulues (disponible
# dans les modèles ass.ok)
# Test des fichiers valides
# Test push pop des registres
# Test avec un include


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

VALID_CODEGEN=./src/test/deca/codegen/valid/sansObjet
rm -f $VALID_CODEGEN/*.ass 2>/dev/null

# Test valides
nb=$(ls -l $VALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests valides ($nb)"
for i in "$VALID_CODEGEN"/*.deca
do
    decac ./$i || exit 1
done

for i in "$VALID_CODEGEN"/*.ass
do
    resultat=$(ima ./$i)
    if [ "$(cat $i.ok)" = "$resultat" ]; then
        echo "OK"
        rm $i
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        rm $i
        exit 1
  fi
done

# Test push pop des registres
decac -r 4 $VALID_CODEGEN/pushpop/pushpop.deca
resultat=$(ima $VALID_CODEGEN/pushpop/pushpop.ass)
if [ "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)" = "$resultat" ]; then
    echo "OK"
    rm $VALID_CODEGEN/pushpop/pushpop.ass
else
    echo "$VALID_CODEGEN/pushpop/pushpop.ass KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)"
    rm $VALID_CODEGEN/pushpop/pushpop.ass
    exit 1
fi

#Test sur un include
decac ./src/test/deca/decompile/Test_include/Include.deca || exit 1
resultat=$(ima ./src/test/deca/decompile/Test_include/Include.ass)

    if [ "$(cat ./src/test/deca/codegen/valid/sansObjet/Include.ass.ok)" = "$resultat" ]; then
        echo "OK"
        rm src/test/deca/decompile/Test_include/Include.ass
    else
        echo "Include.ass KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat ./src/test/deca/codegen/valid/sansObjet/Include.ass.ok)"
        rm rm src/test/deca/decompile/Test_include/Include.ass
        exit 1
  fi

cd "$(dirname "$0")"/../../.. || exit 1
