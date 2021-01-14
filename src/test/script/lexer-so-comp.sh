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

SupprimerCompa(){
    for i in ../../soresultat/*.compa
    do
        rm $i
    done
}
echo "------- Démarrage des tests valide pour lexer"
echo "Test valides:"
for i in *.deca
do
    # est lex valid normalement
    if echo "$i" | grep "_valid" 
    then
        resultat=../../soresultat/"$i".resu
        comparaison=../../soresultat/"$i".compa
        erreur=$(head $i -n 3 | tail -1 | sed 's/\/\///')
        # cree le fichier et regarde si il y a une erreur
        if test_lex $i 2>&1 > $comparaison | grep  -q "$i:"
        then
            echo "Erreur innatendu de test_lex"
            echo $erreur
            SupprimerCompa
            exit 1
        fi
        if diff $comparaison $resultat
        then
             echo "Succes pour test_lex attendu"
        else 
             echo "Echec sortie different de resultat"
             echo "Attendu: "
             echo $erreur
             SupprimerCompa
             exit 1
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
        comparaison=../../soresultat/"$i".compa
        # cree le fichier et regarde si il y a une erreur
        if test_lex $i 2>&1 > $comparaison | grep -q "$i"
        then
            echo "Erreur innatendu de test_lex"
            #exit 1
        fi
        if diff $comparaison $resultat
        then
             echo "Succes pour test_lex attendu"
        else 
        resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
             echo "Echec sortie different de resultat"
             echo "Attendu: "
             echo $resultat
             SupprimerCompa
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
        echo "test du fichier $i"
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

