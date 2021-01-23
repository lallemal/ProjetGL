#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Script de test pour la syntaxe extension.
# (Le lexer n'a pas de test invalide selon la decription de notre grammaire)
# On test tout d'abord que test_synt ne produise pas d'erreur pour les fichiers valides
# On compare ensuite le résultat de test_synt avec un modèle préetablie et vérifié
# On regarde que la bonne erreur est levée dans les tests invalides (l'erreur attendu
# est disponible en première ligne du fichier invalide)



cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur lors de test_synt pour les fichiers valides
nb=$(ls -l src/test/deca/syntax/valid/extension/*.deca| wc -l)
echo "------- Démarrage des tests valide $(($nb))"

for i in src/test/deca/syntax/valid/extension/*.deca
do
  if test_synt $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_synt pour $i"
  else
    echo "OK"
  fi

done

# Test comparaison du résultat de test_synt avec un modèle
nb=$(ls -l src/test/deca/syntax/valid/extension/*.deca| wc -l)
echo "------- Démarrage des tests valide comparaison ($nb)"
for i in src/test/deca/syntax/valid/extension/*.deca
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

# Test de l'erreur dans les tests invalides
nb=$(ls -l src/test/deca/syntax/invalid/extension/*.deca| wc -l)
echo "------ Démarrage des tests invalide ($nb)"
echo "Test invalides:"
for i in src/test/deca/syntax/invalid/extension/*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_synt $i 2>&1 | grep -q -e "$error"
  then
     echo "Echec attendu pour test_synt" 
  else
      #affiche la troisieme ligne de l'entet et 1 ere pour debug info
     resultat=$(head $i -n 3 | tail -1 | sed 's/\/\///')
     echo "Succes inattendu pour test_synt dans $i ,Attendu:"
     echo $resultat
     echo $error
     exit 1
    fi
done
