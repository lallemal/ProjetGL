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
OBRACE : '{' ;
CBRACE : '}' ; 
OPARENT : '(' ;
CPARENT : ')' ;
PRINT : 'print' ;
PRINTLN : 'println' ;
SEMI : ';' ;
COMMA : ',' ; 
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\' )*? '"'; 


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

// symbole speciaux
fragment PLUS : '+';
fragment MINUS : '-';
INF : '<';
SUP : '>';
INFEQ : '<=';
SUPEQ : '>=';
EXCLAM : '!';
INTERRO : '?';
EQUALS : '=';
EGALITE : '==';
DIFF : '!=';
MULT : '*';
MODULO : '%';
SLASH : '/';
DOT : '.';
AND : '&&';
OR : '||';
// litteraux entiers
NUM : DIGIT+;

// mot reserve ne sont pas des ident donc le placer a la fin pour priorite
IDENT : (LETTRE | '$' | '_')(LETTRE | DIGIT | '$' | '_')*;

