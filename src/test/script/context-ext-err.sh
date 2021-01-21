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
INVALID_CONTEXT=src/test/deca/context/invalid/extension
VALID_CONTEXT=src/test/deca/context/valid/extensions
VALID_CODEGEN=src/test/deca/codegen/valid/extension

# /!\ test valide lexicalement, mais invalide pour l'étape A.
# test_lex peut au choix afficher les messages sur la sortie standard
# (1) ou sortie d'erreur (2). On redirige la sortie d'erreur sur la
# sortie standard pour accepter les deux (2>&1)



nb=$(ls -l $INVALID_CONTEXT/*.deca | wc -l)
echo "------- Démarrage des tests invalide ($nb)"
for i in "$INVALID_CONTEXT"/*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_context $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour test_context"
  elif test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context : $i"
    exit 1
  else
    echo "Succès inattendu pour test_context : $i"
    exit 1
  fi

done


nb=$(ls -l $VALID_CONTEXT/*.deca | wc -l)
echo "------ Démarrage des tests valide ($nb)"
for i in "$VALID_CONTEXT"/*.deca
do
  if test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context : $i"
    exit 1
  else
    echo "OK"
  fi

done


nb=$(ls -l $VALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests valide de codegen (devant aussi être valide normalement) ($nb)"
for i in "$VALID_CODEGEN"/*.deca
do
  if test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context $i"
    exit 1
  else
    echo "OK"
  fi

done





