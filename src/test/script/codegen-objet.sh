#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.


# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

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
 
cd "$(dirname "$0")"/../../.. || exit 1

nb=$(ls -l src/test/deca/codegen/invalid/objet/*.deca | wc -l)
echo "------- Démarrage des tests invalide deca ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if decac $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour decac"
  elif decac $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac : $i"
    exit 1
  else
    echo "Succès inattendu pour decac : $i"
    exit 1
  fi

done

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


