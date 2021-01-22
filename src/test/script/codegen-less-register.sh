#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code.
# On teste des fichiers valides et des fichiers invalides.

# Script testant la génération de code avec l'option decac -r 

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

#####Pour le sans Objet (gencode-so-valide)
VALID_CODEGEN=./src/test/deca/codegen/valid/sansObjetPourScript
rm -f $VALID_CODEGEN/*.ass 2>/dev/null

# Test valides 
nb=$(ls -l $VALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests valides ($nb)"
for i in "$VALID_CODEGEN"/*.deca
do
    decac -r 4 ./$i || exit 1
done

for i in "$VALID_CODEGEN"/*.ass
do
    resultat=$(ima ./$i)
    if [ "$(cat $i.ok)" = "$resultat" ]; then
        echo "$i OK"
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        exit 1
  fi
done

# Test push pop des registres
decac -r 4 $VALID_CODEGEN/pushpop/pushpop.deca
resultat=$(ima $VALID_CODEGEN/pushpop/pushpop.ass)
if [ "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)" = "$resultat" ]; then
    echo "$VALID_CODEGEN/pushpop/pushpop.ass OK"
else
    echo "$VALID_CODEGEN/pushpop/pushpop.ass KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat $VALID_CODEGEN/pushpop/pushpop.ass.ok)"
    exit 1
fi

#Test sur un include
decac -r 4 ./src/test/deca/decompile/Test_include/Include.deca || exit 1
resultat=$(ima ./src/test/deca/decompile/Test_include/Include.ass)

    if [ "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Include.ass.ok)" = "$resultat" ]; then
        echo "$INVALID_CODEGEN/Include.ass OK"
    else
        echo "$INVALID_CODEGEN/Include.ass KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat ./src/test/deca/codegen/valid/sansObjetPourScript/Include.ass.ok)"
        exit 1
  fi

cd "$(dirname "$0")"/../../.. || exit 1


##### Test pour sans Objet (gencode-so-invalide)

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

INVALID_CODEGEN=./src/test/deca/codegen/invalid/sansObjetPourScript
rm -f $INVALID_CODEGEN/*.ass 2>/dev/null


# On exécute decac pour avoir les .ass
nb=$(ls -l $INVALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests invalides ($((nb+4)))"
for i in "$INVALID_CODEGEN"/*.deca
do
    decac -r 4 ./$i || exit 1
done
#on met a part le test de debordement de pile car il necessite une execution de ima -p nnn
decac -r 4 $INVALID_CODEGEN/debordementPile/pilePleine.deca || exit 1


# On vérifie qu'on obtient le résultat voulu en comparant avec les modèles (.ass.ok)
for i in "$INVALID_CODEGEN"/*.ass
do
    resultat=$(ima ./$i)
    if [ "$(cat $i.ok)" = "$resultat" ]; then
        echo "$i OK"
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        exit 1
  fi
done

#on met a part le test de debordement de pile car il necessite une execution de ima -p nnn
resultat=$(ima -p 003 $INVALID_CODEGEN/debordementPile/pilePleine.ass)
if [ "$(cat $INVALID_CODEGEN/debordementPile/pilePleine.ass.ok)" = "$resultat" ]; then
    echo "$INVALID_CODEGEN/debordementPile/pilePeine.ass OK"
else
    echo "$INVALID_CODEGEN/debordementPile/pilePeine.ass KO ->"
    echo "Résultat innatendu, le résultat:"
    echo "$resultat"
    echo "ce qui était attendu:"
    echo "$(cat $INVALID_CODEGEN/debordementPile/pilePleine.ass.ok)"
    exit 1
fi


##### Test pour objet (gencode-objet)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur avec decac et fichier valide
nb=$(ls -l src/test/deca/codegen/valid/objet/*.deca | wc -l)
echo "------ Démarrage des tests valide decac -r 4 ($((nb)))"
for i in  src/test/deca/codegen/valid/objet/*.deca
do
  if decac -r $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac -r 4 pour $i"
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
    echo "Echec inattendu pour decac -r 4 pour $i"
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
echo "------ Démarrage des tests valide avec changement du nombre de regsitre decac -r 4"
if decac -r 4 src/test/deca/codegen/valid/objet/Registre/Registre.deca 2>&1 | grep -q -e "$i:"
then
  echo "Echec inattendu pour decac -r 4 pour Registre.deca"
else
  echo "OK"
fi


# Test de registres avec ima
echo "------ Démarrage des tests valide avec changement du nombre de regsitre ima "
if ima src/test/deca/codegen/valid/objet/Registre/Registre.ass 2>&1 | grep -q -e "$i:"
then
  echo "Echec inattendu pour decac -r 4 pour Registre.ass"
else
  echo "OK"
fi

# Test de registres comparaison avec ima
echo "------ Démarrage des tests valide avec changement du nombre de registre ima "
resultat=$(ima src/test/deca/codegen/valid/objet/Registre/Registre.ass)
if [ "$(less src/test/deca/codegen/valid/objet/Registre/Registre.ass.ok)" = "$resultat" ]; then
        echo "OK"
  else
        echo "Résultat innatendu pour Registre.ass, le résultat:"
        echo "$(resultat)"
        echo "ce qui était attendu:"
        echo "$(less src/test/deca/codegen/valid/objet/Registre/Registre.ass.ok)"
        exit 1
  fi


cd "$(dirname "$0")"/../../.. || exit 1

#On test que l'erreur des fichier invalide soit celle attendu (disponible en première ligne)
nb=$(ls -l src/test/deca/codegen/invalid/objet/Error/*.deca | wc -l)
echo "------- Démarrage des tests invalide ima ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/Error/*.deca
do
    decac -r 4 $i
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
echo "------- Démarrage des test de débordement de pile decac -r 4 ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementPile/*.deca
do 
    if decac -r 4 $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac -r 4 pour $i"
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
echo "------- Démarrage des test de débordement de tas decac -r 4 ($((nb)))"
for i in src/test/deca/codegen/invalid/objet/debordementTas/*.deca
do 
    if decac -r 4 $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac -r 4 pour $i"
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


##### Test pour l'extension (gencode-extension)

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test pas d'erreur avec decac et fichier valide
nb=$(ls -l src/test/deca/codegen/valid/extension/*.deca | wc -l)
echo "------ Démarrage des tests valide decac -r 4 ($((nb)))"
for i in  src/test/deca/codegen/valid/extension/*.deca
do
  if decac -r 4 $i 2>&1 | grep -q -e "$i:"
  then
    echo "Echec inattendu pour decac -r 4 pour $i"
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
    echo "Echec inattendu pour decac -r 4 pour $i"
  else
    echo "OK"
  fi

done

