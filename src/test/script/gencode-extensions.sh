#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.

# Script de test pour la génération de code pour l'extension.
# On test tout d'abord que decac ne produise pas d'erreur pour les fichiers valides
# On fait de même avec ima et les fichiers valides
# On compare ensuite le résultat de ima avec un modèle préetablie et vérifié
# On test certain fichier avec moins de registres (decac -r)
# On regarde que la bonne erreur est levée dans les tests invalides avec ima(l'erreur attendu
# est disponible en première ligne du fichier invalide)
# On test les débordements de pile
# On test les débordements de tas


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur avec decac et fichier valide
nb=$(ls -l src/test/deca/codegen/valid/extension/*.deca | wc -l)
echo "------ Démarrage des tests valide decac ($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.deca
do
  if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done

# Test pas d'erreur avec ima et fichier valide
echo "------ Démarrage des tests valide ima ($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.ass
do
  if ima $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi

done