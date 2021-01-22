#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code en sans objet FICHIERS VALIDES. 
# On génere les .ass puis on compare les .ass obtenus à celle voulues (disponible
# dans les modèles ass.ok)
# Test des fichiers valides
# Test push pop des registres
# Test avec un include


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

VALID_CODEGEN=./src/test/deca/codegen/valid/sansObjetPourScript
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
        echo "$i OK"
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        exit 1
  fi
done

# Test push pop des registres
decac -r 4 $VALID_CODEGEN/pushpop/pushpop.deca
resultat=$(ima $VALID_CODEGEN/pushpop/pushpop.ass)
if [ "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)" = "$resultat" ]; then
    echo "$VALID_CODEGEN/pushpop/pushpop.ass OK"
else
    echo "$VALID_CODEGEN/pushpop/pushpop.ass KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)"
    exit 1
fi

#Test sur un include
decac ./src/test/deca/decompile/Test_include/Include.deca || exit 1
resultat=$(ima ./src/test/deca/decompile/Test_include/Include.ass)

    if [ "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Include.ass.ok)" = "$resultat" ]; then
        echo "$INVALID_CODEGEN/Include.ass OK"
    else
        echo "$INVALID_CODEGEN/Include.ass KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Include.ass.ok)"
        exit 1
  fi

cd "$(dirname "$0")"/../../.. || exit 1


