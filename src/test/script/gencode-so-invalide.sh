#! /bin/sh

# Auteur : gl40
# Version initiale : 01/01/2021

# Base pour un script de test de la génération de code en sans objet FICHIERS INVALIDES. 
# On génere les .ass puis on compare les erreurs obtenus à celle voulues (disponible
# dans les modèles ass.ok)
# Test des fichiers invalides (div0...)
# Test débordement de pile
# Test NoCheck


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

INVALID_CODEGEN=./src/test/deca/codegen/invalid/sansObjetPourScript
rm -f $INVALID_CODEGEN/*.ass 2>/dev/null


# On exécute decac pour avoir les .ass
nb=$(ls -l $INVALID_CODEGEN/*.deca | wc -l)
echo "------ Démarrage des tests invalides ($((nb+4)))"
for i in "$INVALID_CODEGEN"/*.deca
do
    decac ./$i || exit 1
done
#on met a part le test de debordement de pile car il necessite une execution de ima -p nnn
decac $INVALID_CODEGEN/debordementPile/pilePleine.deca || exit 1


# On vérifie qu'on obtient le résultat voulu en comparant avec les modèles (.ass.ok)
for i in "$INVALID_CODEGEN"/*.ass
do
    resultat=$(ima ./$i)
    if [ "$(cat $i.ok)" = "$resultat" ]; then
        echo "$i OK"
        rm $i
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        rm $i
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


#on met a part le test de nocheck car il necessite une execution de decac -n
for i in $INVALID_CODEGEN/noCheck/*.deca
do
    decac -n ./$i || exit 1
done

for i in $INVALID_CODEGEN/noCheck/*.ass
do
    resultat=$(ima -p 005 ./$i)
    if [ "$(cat $i.ok)" = "$resultat" ]; then
        echo "OK"
        rm $i
    else
        echo "$i KO ->"
        echo "Résultat innatendu, le résultat:"
        echo "$resultat"
        echo "ce qui était attendu:"
        echo "$(cat $i.ok)"
        rm $i
        exit 1
    fi
done

