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

SupprimerComp(){
    for i in src/test/deca/syntax/soresultat/*.comp
    do
        rm $i
    done
}
PATH=./src/test/script/launchers:"$PATH"

nb=$(ls -l | wc -l)
nb=$(($nb-1))
echo "------- Démarrage des tests valide ($nb)"
echo "Test valides:"
for i in src/test/deca/syntax/valid/sansobjet/*.deca
do
    echo "test du fichier $i"
    resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
    # diff renvoi 0 si les fichiers sont identiques
    nom="$(basename -- $i)"
    comparaison=src/test/deca/syntax/soresultat/"$nom".comp
    #cree le fichier comparaison et regarde pas d'erreur dedans
    # si une erreur fichier non valid alors sort 
    if test_synt $i 2>&1 > $comparaison | grep -q -e "$nom:"
    then 
        echo "Erreur detecte Echec inattendu"
        echo "Attendu ":
        echo $resultat
        SupprimerComp
        exit 1
    fi    
    if diff $comparaison src/test/deca/syntax/soresultat/"$nom".res
    then
        echo "Echec Attendu"
    else 
        echo "Attendu ":
        echo $resultat
        SupprimerComp
    fi

done


nb=$(ls -l | wc -l)
((nb=$nb-1))

echo "------ Démarrage des tests invalide ($nb)"
echo "Test invalides:"
for i in src/test/deca/syntax/invalid/sansobjet/*.deca
do
  echo "test du fichier $i"
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_synt $i 2>&1 | grep -q -e "$error"
  then
     echo "Echec attendu pour test_synt" 
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


