#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de context sans objet
# Test pour les fichiers invalides qu'il y a une erreur lors de test_context et 
# vérifie que c'est l'erreur attendue (disponible en première ligne)
# Test pour les fichiers valides qu'aucune erreur lors de test_context
# Test pour les fichiers de codegen qu'aucune erreur lors du test_context
# Test include 


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
INVALID_CONTEXT=src/test/deca/context/invalid/sansobjet
VALID_CONTEXT=src/test/deca/context/valid/sansobjet
VALID_CODEGEN=src/test/deca/codegen/valid/sansObjet


# Test des fichiers invalides
nb=$(ls -l $INVALID_CONTEXT/*.deca | wc -l)
echo "------- Démarrage des tests invalide test_context($nb)"
for i in "$INVALID_CONTEXT"/*.deca
do
  error=$(head $i -n 1 | sed 's/\/\///')
  if test_context $i 2>&1 | grep -q -e "$error"
  then
    echo "Echec attendu pour test_context"
  elif test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context : $i"
    exit 1
  else
    echo "Succès inattendu pour test_context : $i"
    exit 1
  fi

done

# Test des fichiers valides
nb=$(ls -l $VALID_CONTEXT/*.deca | wc -l)
echo "------ Démarrage des tests valide test_context($nb)"
for i in "$VALID_CONTEXT"/*.deca
do
  if test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context : $i"
    exit 1
  else
    echo "OK"
  fi

done

#Test que le résultats des fichiers valides est celui attendu (modèle dispo dans 
# les .deca.ok)
nb=$(ls -l $VALID_CONTEXT/*.deca | wc -l)
echo "------ Démarrage des tests valide comparaison test_context ($nb)"
for i in $VALID_CONTEXT/*.deca
do
    resultat=$(test_context $i)
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


# Test des fichiers de codegen
nb=$(ls -l $VALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests valide de codegen test_context($nb)"
for i in "$VALID_CODEGEN"/*.deca
do
  if test_context $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour test_context $i"
    exit 1
  else
    echo "OK"
  fi

done

# Test include
cd src/test/deca/decompile/Test_include || exit 1

 if test_context Include.deca 2>&1 | grep -q -e "Include.deca:"
  then
    echo "Echec inattendu pour test_context"
  else
    echo "OK"
  fi

cd "$(dirname "$0")"/../../../.. || exit 1






