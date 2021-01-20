#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

VALID_CODEGEN=./src/test/deca/codegen/valid/sansObjetPourScript
rm -f $VALID_CODEGEN/*.ass 2>/dev/null
nb=$(ls -l $VALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests ($nb)"
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
