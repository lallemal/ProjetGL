#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.

# Script de test pour la génération de code pour l'extension.
# On test tout d'abord que decac ne produise pas d'erreur pour les fichiers valides
# On fait de même avec ima et les fichiers valides
# On compare ensuite le résultat de ima avec un modèle préetablie et vérifié
# On test certain fichier avec moins de registres (decac -r)
# On regarde que la bonne erreur est levée dans les tests invalides avec ima(l'erreur attendu
# est disponible en première ligne du fichier invalide)
# On test les débordements de pile



cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur avec decac et fichier valide
nb=$(ls -l src/test/deca/codegen/valid/extension/*.deca | wc -l)
echo "------ Démarrage des tests valide decac ($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.deca
do
  if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done

# Test pas d'erreur avec ima et fichier valide
echo "------ Démarrage des tests valide ima ($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.ass
do
  if ima $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi

done

# Test comparaison des résultat ima avec les modèles (.decac.ok)
echo "------ Démarrage des tests valide ima comparaison avec les modèles OK($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.ass
do
  resultat=$(ima $i)
    if [ "$(less $i.ok)" = "$resultat" ]; then
         echo "OK"
         rm $i
     else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less $i.ok)"    
        rm $i
        exit 1
    fi

done




#On test que l'erreur des fichier invalide soit celle attendu (disponible en première ligne)
nb=$(ls -l src/test/deca/codegen/invalid/extension/*.deca | wc -l)
echo "------- Démarrage des tests invalide ima ($((nb)))"
for i in src/test/deca/codegen/invalid/extension/*.deca
do
    decac $i
done
for i in src/test/deca/codegen/invalid/extension/*.ass
do
  resultat=$(ima $i)
  if [ "$(less $i.ok)" = "$resultat" ]; then
        echo "OK"
        rm $i
  else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less $i.ok)"
        rm $i
        exit 1
  fi
done

# On fait les tests de débordement de pile
nb=$(ls -l src/test/deca/codegen/invalid/extension/debordementPile/*.deca | wc -l)
echo "------- Démarrage des test de débordement de pile decac ($((nb)))"
for i in src/test/deca/codegen/invalid/extension/debordementPile/*.deca
do 
    if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done 

#Test débordement de tas 
echo "------- Démarrage des test de débordement de pile decac "
if decac src/test/deca/codegen/invalid/extension/debordementPile/PilePleinNewArray.deca 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour PilePleinNewArray.deca"
  else
    echo "OK"
fi
echo "------- Démarrage des test de débordement de pile ima "
resultat=$(ima -p 003 src/test/deca/codegen/invalid/extension/debordementPile/PilePleinNewArray.ass)
if [ "$(cat src/test/deca/codegen/invalid/extension/debordementPile/MessageErreur.ok)" = "$resultat" ]; then
    echo "OK"
    rm src/test/deca/codegen/invalid/extension/debordementPile/PilePleinNewArray.ass
else
    echo "PilePleinNewArray KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat src/test/deca/codegen/invalid/extension/debordementPile/MessageErreur.ok)"
    rm src/test/deca/codegen/invalid/extension/debordementPile/PilePleinNewArray.ass
    exit 1
fi


#Test débordement de tas 
echo "------- Démarrage des test de débordement de tas decac "
if decac src/test/deca/codegen/invalid/extension/debordementTas/TasPleinNewArray.deca 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour TasPleinNewArray.deca"
  else
    echo "OK"
fi
echo "------- Démarrage des test de débordement de tas ima "
resultat=$(ima -t 003 src/test/deca/codegen/invalid/extension/debordementTas/TasPleinNewArray.ass)
if [ "$(cat src/test/deca/codegen/invalid/extension/debordementTas/MessageErreur.ok)" = "$resultat" ]; then
    echo "OK"
    rm src/test/deca/codegen/invalid/extension/debordementTas/TasPleinNewArray.ass
else
    echo "TasPleinNewArray KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat src/test/deca/codegen/invalid/extension/debordementTas/MessageErreur.ok)"
    rm src/test/deca/codegen/invalid/extension/debordementTas/TasPleinNewArray.ass
    exit 1
fi