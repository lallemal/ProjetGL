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
 decac -p src/test/deca/decompile/Test_include/Include.deca > src/test/deca/decompile/ZZRes_inter.deca || exit 1
    resultat=$(decac -p src/test/deca/decompile/ZZRes_inter.deca)
  if [ "$(less src/test/deca/decompile/Include.deca.ok)" = "$resultat" ]; then
        echo "OK"
  else
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(less src/test/deca/decompile/Include.deca.ok)"
        exit 1
  fi

for i in src/test/deca/decompile/*.deca
do
 #si on atteind le fichier intermédiaire(placé en dernier) on a fini tous les tests donc on peut le supprimet
  if [ "$i" = "src/test/deca/decompile/ZZRes_inter.deca" ]
  then
    rm "src/test/deca/decompile/ZZRes_inter.deca"
    
  else
        #ZZRes_inter.deca nous sert de fichier intermédiaire, entre les deux commandes decac -p
        decac -p $i > src/test/deca/decompile/ZZRes_inter.deca || exit 1
        resultat=$(decac -p src/test/deca/decompile/ZZRes_inter.deca)
    if [ "$(less $i.ok)" = "$resultat" ]; then
            echo "OK"
    else
            echo "Résultat innatendu, le résultat:"
            echo "$resultat"
            echo "ce qui était attendu:"
            echo "$(less $i.ok)"
            exit 1
    fi
  fi
done

cd "$(dirname "$0")"/../../.. || exit 1