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

fragment LETTER : 'a' .. 'z' 'A' .. 'Z';
fragment DIGIT : '0' .. '9';

INCLUDE: '#include' .* ';' { doInclude(getText()); };

COMMENTMONO : '//' (~('\n'))* { skip(); };
COMMENT : '/*' .*? '*/' { skip(); };

OBRACE: '{' ;
CBRACE: '}' ;
OPARENT: '(';
CPARENT: ')' ;
SEMI: ';' ;
COMMA: ',' ;

PRINTLN: 'println';
TRUE: 'true';
FALSE: 'false';
THIS: 'this';

FLOAT: DIGIT+ '.' (DIGIT)+;
INT: DIGIT+;

STRING: '"' .*? '"' ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;

DUMMY_TOKEN: .;

 // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
