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

// Deca lexer rules.
COMMENT : (('/*' .*? '*/') | ('//' .*? '\n')) { skip(); } ;
fragment LETTRE : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT : '0'..'9';
fragment SPECIALS : '!' | '?' | ',' | ';' ;
STRING : '"' .*? '"';
OBRACE : '{' ;
CBRACE : '}' ; 
OPARENT : '(' ;
CPARENT : ')' ;
PRINT : 'print' ;
PRINTLN : 'println' ;
SEMI : ';' ;
COMMA : ',' ; 

WS : (' ' | '\n' | '\t' | '\r') { skip(); };
