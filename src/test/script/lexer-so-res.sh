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

cd src/test/deca/syntax/valid/sansobjet|| exit 1
nb=$(ls -l | wc -l)
nb=$(($nb-1))

echo "------- Démarrage des tests valide pour lexer($nb)"
echo "Test valides:"
for i in *.deca
do
    # est lex valid normalement
    if echo "$i" | grep "_valid" 
    then
        resultat=../../soresultat/"$i".resu
        # cree le fichier et regarde si il y a une erreur
        if test_lex $i 2>&1 > $resultat | grep -e -q "$i"
        then
            echo "Erreur innatendu de test_lex"
           # exit 1
        fi
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
        echo "test du fichier $i"
        resultat=../../soresultat/"$i".resu
        # cree le fichier et regarde si il y a une erreur
        if test_lex $i 2>&1 > $resultat | grep -e -q "$i"
        then
            echo "Erreur innatendu de test_lex"
            #exit 1
        fi
    fi
done
