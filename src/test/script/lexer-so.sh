#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Script de test pour lexer.
# On test tout d'abord que test_lex ne produise pas d'erreur pour les fichiers valides
# On compare ensuite le résultat de test_synt avec un modèle préetablie et vérifié
# On regarde que la bonne erreur est levée dans les tests invalides (l'erreur attendu
# est disponible en première ligne du fichier invalide)


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


cd src/test/deca/syntax/valid/sansobjet|| exit 1

# Test pour voir si il y a une erreur en lançant test_lex
# Dans le dossier valid/sansobjet
echo "------- Démarrage des tests valide pour lexer test_lex"
echo "Test valides dans le dossier valid/sansobjet:"
for i in *.deca
do
    if test_lex $i 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_lex sur $i."
        exit 1
    else
        echo "OK"
        
    fi
done

# test lexeur valid dans dossier parseur invalid

cd "$(dirname "$0")"/../../.. || exit 1

cd src/test/deca/syntax/invalid/sansobjet || exit 1

echo "Test valides dans le dossier invalid/sansobjet:"
for i in *.deca
do
    # est lex valid normalement
    if echo "$i" | grep "_valid" 
    then
        if test_lex $i 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
        then
            echo "Echec inattendu pour test_lex sur $i."
            exit 1
        else
            echo "OK"
        
        fi
    fi
done

cd "$(dirname "$0")"/../../.. || exit 1


# Test pour voir que le résultat de test_lex est bien celui attendu

echo "------- Démarrage des tests valide pour lexer comparaison"
for i in src/test/deca/syntax/valid/sansobjet/*.deca
do
    # est lex valid normalement
    resultat=$(test_lex $i)
    if [ "$(less $i.resu)" = "$resultat" ]; then
         echo "OK"
     else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less $i.resu)"
        exit 1
    fi
done

# test lexeur valid dans dossier parseur invalid
cd "$(dirname "$0")"/../../.. || exit 1
cd src/test/deca/syntax/invalid/sansobjet || exit 1

for i in *.deca
do
    # est lex valid normalement
    if echo "$i" | grep "_valid" 
    then
        resultat=$(test_lex $i)
        if [ "$(less $i.resu)" = "$resultat" ]; then
             echo "OK"
        else
            echo "Résultat innatendu pour $i, le résultat:"
            echo "$resultat"
            echo "ce qui était attendu:"
            echo "$(less $i.resu)"
            exit 1
        fi
    fi
done


# Test les lexer invalid (lexer et parser meme erreur erreur du lexeur)
echo "------- Démarrage des tests invalid pour lexer"
for i in *.deca
do
    if echo "$i" | grep "_invalid" 
        then
        error=$(head $i -n 1 | sed 's/\/\///')
        if test_lex $i 2>&1 | grep -q -e "$error"
        then
             echo "Echec attendu pour test_lex" 
        else
            #affiche la troisieme ligne de l'entet et 1 ere pour debug info
            resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
            echo "Succes inattendu pour test_lex ,Attendu:"
            echo $resultat
            echo $error
            exit 1
        fi
    fi   
done
