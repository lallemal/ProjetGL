# Projet Génie Logiciel, Ensimag.

## Première phase : Sans Objet

Le compilateur fonctionne comme spécifié dans la documentation [Decac] : 

`decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]
. -b (banner) : affiche une bannière indiquant le nom de l’équipe
. -p (parse) : arrête decac après l’étape de construction de
l’arbre, et affiche la décompilation de ce dernier
(i.e. s’il n’y a qu’un fichier source à
compiler, la sortie doit être un programme
deca syntaxiquement correct)
. -v (verification) : arrête decac après l’étape de vérifications
(ne produit aucune sortie en l’absence d’erreur)
. -n (no check) : supprime les tests de débordement à l’exécution
- débordement arithmétique
- débordement mémoire
- déréférencement de null
. -r X (registers) : limite les registres banalisés disponibles à
R0 ... R{X-1}, avec 4 <= X <= 16
. -d (debug) : active les traces de debug. Répéter
l’option plusieurs fois pour avoir plus de
traces.
. -P (parallel) : s’il y a plusieurs fichiers sources,
lance la compilation des fichiers en
parallèle (pour accélérer la compilation)`

Un lancement de `decac` sans argument permet d'avoir l'aide liée au compilateur.





gl40, 18/01/2021.
