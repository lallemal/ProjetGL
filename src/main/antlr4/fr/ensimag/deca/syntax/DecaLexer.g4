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

STRING : '"' (STRING_CAR | '\\\\' | '\\"' )*? '"';
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

// identificateurs
fragment DIGIT : '0'..'9';

// mot reserve
ASM : 'asm';
EXTENDS : 'extends';
IF : 'if';
ELSE : 'else';
FALSE : 'false';
TRUE : 'true';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
WHILE : 'while';

// mot reserve deca avec objet a decommenter pour l'ajouter
RETURN : 'return'; 
INSTANCEOF : 'instanceof'; 

NEW : 'new';
THIS : 'this';
NULL : 'null';
CLASS : 'class';

// symbole speciaux
PLUS : '+';
MINUS : '-';
LT : '<';
GT : '>';
LEQ : '<=';
GEQ : '>=';
EXCLAM : '!';
EQUALS : '=';
EQEQ : '==';
NEQ : '!=';
TIMES : '*';
PERCENT : '%';
SLASH : '/';
AND : '&&';
OR : '||';

// que avec objet
DOT : '.';

// Litteraux flottants
fragment NUM : DIGIT+;
fragment DEC : NUM '.' NUM;
fragment SIGN : '+' | '-';
// prendre en compte si signe = ''
fragment EXP :   ('E' | 'e') NUM | ('E' | 'e') SIGN NUM  ;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f') | (DEC | DEC EXP);
fragment DIGITHEX : '0'..'9' | 'A'..'F' | 'a'..'f';
fragment NUMHEX : DIGITHEX+;
// ici quatre expressions
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f') |
('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM |
('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') NUM ('F' | 'f') |
('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') NUM ; 
FLOAT : FLOATDEC {isDecimal(getText());} | FLOATHEX{ isHexadecimal(getText()); };


// litteraux entiers doit lever erreur si trop grand
fragment POSITIVE_DIGIT : '1'..'9';
INT : '0' | POSITIVE_DIGIT DIGIT* {intCondition(getText());};  


// mot reserve ne sont pas des ident donc le placer a la fin pour priorite
IDENT : (LETTRE | '$' | '_')(LETTRE | DIGIT | '$' | '_')*;

// inclusion fichier
fragment FILENAME : (LETTRE | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {doInclude(getText());};

// Litteraux extension
LHOOK: '[';
RHOOK: ']';

// Déclarer un tableau \ex: int[] tab = new int[27]; 
// DECL_ARRAY:((FLOAT) LHOOK (FLOAT)? RHOOK | (INT) LHOOK (INT)? RHOOK);

// fragment IDENT_WITHOUT_STRING: (IDENT ~('string')); 
// Déclaration d'un tableau "a la main" \ex int[4] tab = { 1, 2, 2, 4 };
// ARRAY: OBRACE ( (IDENT) (COMMA IDENT)+ )? CBRACE;

// fragment ARRAY_INT: OBRACE ((INT) (COMMA INT)+ )? CBRACE;
// fragment ARRAY_FLOAT: OBRACE ((FLOAT) (COMMA FLOAT)+ )? CBRACE;


// MATRIX: OBRACE ((ARRAY_INT)+ | (ARRAY_FLOAT)+ | (ARRAY_BOOLEAN)+ ) CBRACE;

// MATRIX: OBRACE ARRAY ( COMMA ARRAY )* CBRACE;





