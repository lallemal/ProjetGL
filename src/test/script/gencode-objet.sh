#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.

# Script de test pour la génération de code pour l'objet.
# On test tout d'abord que decac ne produise pas d'erreur pour les fichiers valides
# On fait de même avec ima et les fichiers valides
# On compare ensuite le résultat de ima avec un modèle préetablie et vérifié
# On test certain fichier avec moins de registres (decac -r)
# On regarde que la bonne erreur est levée dans les tests invalides avec ima(l'erreur attendu
# est disponible en première ligne du fichier invalide)
# On test les débordements de pile
# On test les débordements de tas


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur avec decac et fichier valide
nb=$(ls -l src/test/deca/codegen/valid/objet/*.deca | wc -l)
echo "------ Démarrage des tests valide decac ($((nb)))"
for i in  src/test/deca/codegen/valid/objet/*.deca
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
for i in  src/test/deca/codegen/valid/objet/*.ass
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
for i in  src/test/deca/codegen/valid/objet/*.ass
do
  resultat=$(ima $i)
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

# Test de registres avec decac
echo "------ Démarrage des tests valide avec changement du nombre de regsitre decac "
if decac -r 4 src/test/deca/codegen/valid/objet/Registre/Registre.deca 2>&1 | grep -q -e "$i:"
then
  echo "Echec inattendu pour decac pour Registre.deca"
else
  echo "OK"
fi


# Test de registres avec ima
echo "------ Démarrage des tests valide avec changement du nombre de regsitre ima "
if ima src/test/deca/codegen/valid/objet/Registre/Registre.ass 2>&1 | grep -q -e "$i:"
then
  echo "Echec inattendu pour decac pour Registre.ass"
else
  echo "OK"
fi


cd "$(dirname "$0")"/../../.. || exit 1

#On test que l'erreur des fichier invalide soit celle attendu (disponible en première ligne)
nb=$(ls -l src/test/deca/codegen/invalid/objet/Error/*.deca | wc -l)
echo "------- Démarrage des tests invalide ima ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/Error/*.deca
do
    decac $i
done
for i in src/test/deca/codegen/invalid/objet/Error/*.ass
do
  resultat=$(ima $i)
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

# On fait les tests de débordement de pile
nb=$(ls -l src/test/deca/codegen/invalid/objet/debordementPile/*.deca | wc -l)
echo "------- Démarrage des test de débordement de pile decac ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementPile/*.deca
do 
    if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done 

echo "------- Démarrage des test de débordement de pile ima ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementPile/*.ass
do
    resultat=$(ima -p 003 $i)
    if [ "$(cat src/test/deca/codegen/invalid/objet/debordementPile/MessageErreur.ok)" = "$resultat" ]; then
        echo "OK"
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat src/test/deca/codegen/invalid/objet/debordementPile/MessageErreur.ok)"
        exit 1
fi
done

# On fait les tests de débordement de tas
nb=$(ls -l src/test/deca/codegen/invalid/objet/debordementTas/*.deca | wc -l)
echo "------- Démarrage des test de débordement de tas decac ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementTas/*.deca
do 
    if decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac pour $i"
  else
    echo "OK"
  fi
done 

echo "------- Démarrage des test de débordement de tas ima ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementTas/*.ass
do 
resultat=$(ima -t 003 $i)
if [ "$(cat src/test/deca/codegen/invalid/objet/debordementTas/MessageErreur.ok)" = "$resultat" ]; then
    echo "OK"
else
    echo "$i KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat src/test/deca/codegen/invalid/objet/debordementTas/MessageErreur.ok)"
    exit 1
fi
done


