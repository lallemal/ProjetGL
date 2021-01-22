#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Script de test pour la syntaxe objet.
# On test tout d'abord que test_synt ne produise pas d'erreur pour les fichiers valides
# On compare ensuite le résultat de test_synt avec un modèle préetablie et vérifié
# On regarde que la bonne erreur est levée dans les tests invalides (l'erreur attendu
# est disponible en première ligne du fichier invalide)



cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


# Test pas d'erreur lors de test_synt pour les fichiers valides
nb=$(ls -l src/test/deca/syntax/valid/objet/*.deca| wc -l)
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


# Test l'erreur des tests invalides
nb=$(ls -l src/test/deca/syntax/invalid/objet/*.deca | wc -l)
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

