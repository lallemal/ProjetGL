#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la decompilation.
# On test différent fichier et on les compare au résultat attendu (disponible
# dans le dossiers src/test/deca/decompile/Modeles_OK)
# Ici on a pas de test invalide car les tests sont déjà déclarés valide ou
# invalide par le lexer et le parser.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

nb=$(ls -l src/test/deca/decompile/*.deca | wc -l)
echo "------ Démarrage des tests ($((nb+1)))"
resultat=$(decac -p src/test/deca/decompile/Test_include/Include.deca)
  if [ "$(less src/test/deca/decompile/Include.deca.ok)" = "$resultat" ]; then
        echo "OK"
  else
        echo "Résultat innatendu pour $i, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less src/test/deca/decompile/Include.deca.ok)"
        exit 1
  fi
for i in src/test/deca/decompile/*.deca
do
    resultat=$(decac -p $i)
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
 
echo "Pour plus de détails: decac -p fichier_à_étudier"
