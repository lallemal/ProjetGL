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

# /!\ test valide lexicalement, mais invalide pour l'étape A.
# test_lex peut au choix afficher les messages sur la sortie standard
# (1) ou sortie d'erreur (2). On redirige la sortie d'erreur sur la
# sortie standard pour accepter les deux (2>&1)


cd src/test/deca/context/invalid/sansobjet || exit 1
nb=$(ls -l *.deca | wc -l)
echo "------- Démarrage des tests invalide ($nb)"
for i in *.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_context $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour test_context"
  elif test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context"
    exit 1
  else
    echo "Succès inattendu pour test_context"
    exit 1
  fi

done
cd ../../../../../.. || exit 1

cd src/test/deca/context/valid/sansobjet || exit 1
nb=$(ls -l *.deca | wc -l)
echo "------ Démarrage des tests valide ($nb)"
for i in *.deca
do
  if test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context"
    exit 1
  else
    echo "OK"
  fi

done
cd "$(dirname "$0")"/../../../.. || exit 1
