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

cd src/test/deca/syntax/valid/ || exit 1

echo "Test valides:"
for i in *.deca
do
  if test_synt $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_synt pour $i"
  else
    echo "OK"
  fi

done
cd "$(dirname "$0")"/../../.. || exit 1

cd src/test/deca/syntax/invalid/ || exit 1

echo "Test invalides:"
for i in *.deca
do
  if test_synt $i 2>&1 | grep -q -e "$i:"
  then
    resultat=$(sed -n 1p $i)
    if [ "$(less ValideLexerInvalidParser.test)" = "$resultat" ]
    then 
         echo "Echec attendu pour test_synt (valide pour le lexer mais pas pour le parser)"
    else
        echo "Echec attendu pour test_synt"
    fi
  else
    echo "Erreur non detectee par test_synt pour $i"
    exit 1
  fi
done

cd "$(dirname "$0")"/../../.. || exit 1
