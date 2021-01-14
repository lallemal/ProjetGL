#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la lexicographie.
# On teste un fichier valide et un fichier invalide.
# Il est conseillé de garder ce fichier tel quel, et de créer de
# nouveaux scripts (en s'inspirant si besoin de ceux fournis).


# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

cd src/test/deca/codegen/valid/sansObjetPourScript || exit 1
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
