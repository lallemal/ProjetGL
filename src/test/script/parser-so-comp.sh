#! /bin/sh

SupprimerComp(){
    rm *.comp
}
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
SupprimerComp
nb=$(ls -l | wc -l)
nb=$(($nb-1))
echo "------- Démarrage des tests valide ($nb)"
echo "Test valides:"
for i in *.deca
do
    echo "test du fichier $i"
    # diff renvoi 0 si les fichiers sont identiques
    resultat=../../soresultat/"$i".res
    comparaison=../../soresultat/"$i".comp
    #cree le fichier comparaison et regarde pas d'erreur dedans
    if test_synt $i 2>&1 > $comparaison | grep -e -q "$i"
    then 
        echo "Erreur detecte Echec inattendu"
        echo "Attendu ":
        echo $resultat
        SupprimerComp
        exit 1
    fi    
    if diff $resultat $comparaison
    then
        echo "Succes pour test_synth attendu"
    else 
        resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
        echo "Echec inattendu"
        echo "Attendu: "
        echo $resultat
        SupprimerComp
        exit 1
    fi 

done
cd "$(dirname "$0")"/../../.. || exit 1

cd src/test/deca/syntax/invalid/sansobjet || exit 1

nb=$(ls -l | wc -l)
((nb=$nb-1))

echo "------ Démarrage des tests invalide ($nb)"
echo "Test invalides:"
for i in *.deca
do
  echo "test du fichier $i"
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_synt $i 2>&1 | grep -q -e "$error"
  then
     echo "Echec attendu pour test_synt" 
     exit 1
  else
      #affiche la troisieme ligne de l'entet et 1 ere pour debug info
     resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
     echo "Succes inattendu pour test_synt ,Attendu:"
     echo $resultat
     echo $error
     SupprimerComp
     exit 1
    fi
done
SupprimerComp


