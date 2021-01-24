#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la bibliothèque  FICHIERS VALIDES.
# On génere les .ass puis on compare les .ass obtenus à celle voulues (disponible
# dans les modèles ass.ok)
# Test des fichiers valides
# Test push pop des registres
# Test avec un include


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

VALID_CODEGEN=./src/test/deca/codegen/valid/biblio
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



cd "$(dirname "$0")"/../../.. || exit 1
