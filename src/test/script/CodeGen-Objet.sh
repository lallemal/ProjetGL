#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.


# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

nb=$(ls -l src/test/deca/codegen/valid/objet/*.deca | wc -l)
echo "------ Démarrage des tests valide decac $((nb))"
for i in  src/test/deca/codegen/valid/objet/*.deca
do
  if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done

echo "------ Démarrage des tests valide ima $((nb))"
for i in  src/test/deca/codegen/valid/objet/*.ass
do
  if ima $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi

done
 
cd "$(dirname "$0")"/../../.. || exit 1

nb=$(ls -l src/test/deca/codegen/invalid/objet/*.deca | wc -l)
echo "------- Démarrage des tests invalide ($nb)"
for i in src/test/deca/codegen/invalid/objet//*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if decac $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour decac"
  elif decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac : $i"
    exit 1
  else
    echo "Succès inattendu pour decac : $i"
    exit 1
  fi

done