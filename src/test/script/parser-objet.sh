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


nb=$(ls -l | wc -l)
echo "------- Démarrage des tests valide $(($nb-1))"

for i in src/test/deca/syntax/valid/objet/*.deca
do
  if test_synt $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_synt pour $i"
  else
    echo "OK"
  fi

done


nb=$(ls -l | wc -l)
echo "------- Démarrage des tests invalide $(($nb-1))"

for i in src/test/deca/syntax/invalid/objet/*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_synt $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour test_synt"
  elif test_synt $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_synt dans $i"
  else
    echo "Succès inattendu pour test_synt dans $i"
  fi
done

