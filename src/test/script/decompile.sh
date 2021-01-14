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

cd src/test/deca/decompile
nb=$(ls -l *.deca | wc -l)
echo "------ Démarrage des tests ($nb)"
for i in *.deca
do
  resultat=$(decac -p $i)
  if [ "$(less Modeles_OK/$i.ok)" = "$resultat" ]; then
        echo "$i ok"
  else
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less Modeles_OK/$i.ok)"
        exit 1
  fi
done
cd "$(dirname "$0")"/../../.. || exit 1
echo "Pour plus de détails: decac -p fichier_à_étudier"