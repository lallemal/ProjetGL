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
