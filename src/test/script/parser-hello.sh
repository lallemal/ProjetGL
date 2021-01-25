#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Script de test pour le parser dans hello.
# Test des fichiers valides (pas d'erreur lors du test_synt)
# Test des fichiers valides (comparaison avec la valeur attendue)
# Test des fichiers invalides (la bonne erreur est obtenue)


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


# On test que les fichiers valide passe avec test_synt
echo "Test_synt pour les fichiers valide:"
for i in src/test/deca/syntax/valid/hello/*.deca
do
  if test_synt $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_lex"
    exit 1
  else
    echo "OK"
  fi

done

# On test que le résultat est bien celui qu'on attend
# les modèles sont contenus dans les fichier .deca.ok
echo "Comparaison de résultat avec test_lex:"
for i in src/test/deca/syntax/valid/hello/*.deca
do
    resultat=$(test_synt $i)
    if [ "$(less $i.ok)" = "$resultat" ]; then
         echo "OK"
     else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less $i.ok)"
        exit 1
    fi

done

#On test les fichiers invalide(ils ont l'erreur attendu, présente en première ligne du fichier)
#(On ne test pas les deux fichiers hors du dossier Lexer_Valid car ils ne passent déjà pas test_lex)
echo "Test fichiers invalides"
for i in src/test/deca/syntax/invalid/hello/Valide_Lexer/*.deca
do 
    error=$(head $i -n 1 | sed 's/\/\///')
    if test_synt $i 2>&1 | grep -q -e "$i:"
    then
        echo "Echec attendu pour test_synt"
    else
        echo "Succès inattendu"
        exit 1
  fi

done

