#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Script de test pour le lexer dans hello.
# Test des fichiers donnés
# Test des fichiers valides (pas d'erreur lors du test_lex)
# Test des fichiers valides (comparaison avec la valeur attendue)


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# /!\ test valide lexicalement, mais invalide pour l'étape A.
# test_lex peut au choix afficher les messages sur la sortie standard
# (1) ou sortie d'erreur (2). On redirige la sortie d'erreur sur la
# sortie standard pour accepter les deux (2>&1)
if test_lex src/test/deca/syntax/invalid/provided/simple_lex.deca 2>&1 \
    | head -n 1 | grep -q 'simple_lex.deca:[0-9]'
then
    echo "Echec inattendu de test_lex"
    exit 1
else
    echo "OK"
fi

# Ligne 10 codée en dur. Il faudrait stocker ça quelque part ...
if test_lex src/test/deca/syntax/invalid/provided/chaine_incomplete.deca 2>&1 \
    | grep -q -e 'chaine_incomplete.deca:10:'
then
    echo "Echec attendu pour test_lex"
else
    echo "Erreur non detectee par test_lex pour chaine_incomplete.deca"
    exit 1
fi

# Ligne encore codée en dur

if test_lex src/test/deca/syntax/invalid/hello/commentaire_simple_deux_lignes.deca 2>&1 \
    | grep -q -e 'commentaire_simple_deux_lignes.deca:3:'
then
    echo "Echec attendu pour test_lex"
else
    echo "Erreur non detectee par test_lex pour commentaire_simple_deux_lignes.deca"
    exit 1
fi

if test_lex src/test/deca/syntax/invalid/hello/commentaire_non_ouvert.deca 2>&1 \
    | grep -q -e 'commentaire_non_ouvert.deca:1:'
then
    echo "Echec attendu pour test_lex"
else
    echo "Erreur non detectee par test_lex pour  commentaire_non_ouvert.deca"
    exit 1
fi



# On test que les fichiers avec noté "valide pour le lexer" passent bien le test_lex
echo "Test_lex pour les fichiers valide:"
for i in src/test/deca/syntax/valid/hello/*.deca
do
  if test_lex $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_lex"
    exit 1
  else
    echo "OK"
  fi

done

for i in src/test/deca/syntax/invalid/hello/Valide_Lexer/*.deca
do 
    if test_lex $i 2>&1 | grep -q -e "$i:"
    then
        echo "Echec inattendu pour test_lex"
        exit 1
    else
        echo "OK"
  fi

done

# On test que le résultat est bien celui qu'on attend
# les modèles sont contenus dans les fichier .deca.lex.ok
echo "Comparaison de résultat avec test_lex:"
for i in src/test/deca/syntax/valid/hello/*.deca
do
    resultat=$(test_lex $i)
    if [ "$(less $i.lex.ok)" = "$resultat" ]; then
         echo "OK"
     else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less $i.lex.ok)"
        exit 1
    fi

done

# les modèles sont contenus dans les fichier .deca.ok
for i in src/test/deca/syntax/invalid/hello/Valide_Lexer/*.deca
do 
     resultat=$(test_lex $i)
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