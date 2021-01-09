lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {

}

// Deca lexer rules. print('hello World')

COMMENT : (('/*' .*? '*/') | ('//' .*? '\n')) { skip(); } ;
fragment LETTRE : 'a'..'z' | 'A'..'Z' ;
fragment SPECIALS : '!' | '?' | ',' | ';' ;
fragment STRING_CAR : ~('"' | '\\' | '\n' ) ;
STRING : '"' (STRING_CAR | '\\' )*? '"';
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\' )*? '"'; 

// caractere speciaux
OBRACE : '{' ;
CBRACE : '}' ; 
OPARENT : '(' ;
CPARENT : ')' ;
SEMI : ';' ;
COMMA : ',' ; 

// mot reserve
PRINT : 'print' ;
PRINTLN : 'println' ;



WS : (' ' | '\n' | '\t' | '\r') { skip(); };
// Deca lexer rules sans objets

// identificateurs
fragment DIGIT : '0'..'9';

// mot reserve
ASM : 'asm';
EXTENDS : 'extends';
IF : 'if';
ELSE : 'else';
FALSE : 'false';
TRUE : 'true';
READINT : 'readint';
READFLOAT : 'readFloat';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
WHILE : 'while';

// mot reserve deca avec objet a decommenter pour l'ajouter
//RETURN : return; 
//INSTANCEOF : instanceof; 
//NEW : new;
//THIS : this;
//NULL : null;
//CLASS : class;

// symbole speciaux
PLUS : '+';
MINUS : '-';
INF : '<';
SUP : '>';
INFEQ : '<=';
SUPEQ : '>=';
EXCLAM : '!';
EQUALS : '=';
EGALITE : '==';
DIFF : '!=';
MULT : '*';
MODULO : '%';
SLASH : '/';
AND : '&&';
OR : '||';

// que avec objet
//DOT : '.';

// litteraux entiers doit lever erreur si trop grand
fragment POSITIVE_DIGIT : '1'..'9';
INT : '0' | POSITIVE_DIGIT DIGIT* {intCondition(getText());};  

// Litteraux flottants
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | ' ';
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | ' ');
fragment DIGITHEX : 'O'..'9' | 'A'..'F' | 'a'..'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | ' ' );
FLOAT : FLOATDEC | FLOATHEX;


// mot reserve ne sont pas des ident donc le placer a la fin pour priorite
IDENT : (LETTRE | '$' | '_')(LETTRE | DIGIT | '$' | '_')*;

// inclusion fichier
fragment FILENAME : (LETTRE | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {doInclude(getText());};


