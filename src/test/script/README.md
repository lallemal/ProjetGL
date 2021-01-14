Test de maniere incrementale
# Test
Partie A


Partie sans objet

entete fichier test
valid/deca

// 
//Descrption:
//Resultats:

invalid/deca

//output attendu
//Description:
//resultat: ligne 
fichier res deca de sotcke dans syntax/soresultat
fichier deca a tester dans valid/deca
                      invalid/deca 
les fichiers valid/deca sont teste par comparaison de fichier (car output souvent long)
les fichier invalid/deca par comparaison avec le message d'erreur attendu
Test DecaLexer
    meme principe

Test DecaParser
# pour voir si l'arbre est correct parser/valid on stocke dans fichier et on compare avec .res 
script:
    parser-so-res.sh : genere fichier resultat correct servant a comparer
    parser-so-comp.sh: genere fichier .comp, si ok meme que .res
    si test ne passe pas, affiche le resultats attendu: ligne 3 de l'entete

    lexer-so-res.sh
    lexer-so-comp.sh



 

