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

fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0' .. '9';

fragment SLAS: '\\"';
fragment SLASS: '\\\\';

fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
fragment STRING_CAR: (~('\n' | '\\'));

fragment NUM : DIGIT+;
fragment SIGN: '+' | '-' ;
fragment EXP: ( 'E' | 'e' ) SIGN? NUM;
fragment DEC: NUM '.' NUM;
fragment FI: ('F' | 'f');
fragment FLOATDEC: (DEC | DEC EXP) FI?;
fragment DIGITHEX: DIGIT | 'A'  ..  'F' | 'a'  ..  'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN? NUM FI?;



INCLUDE: '#include' (' ')* '"' FILENAME '"' { doInclude(getText()); };


READINT: 'readInt';
READFLOAT: 'readFloat';


COMMENTMONO: '//' (~('\n'))* { skip(); };
COMMENT: '/*' .*? '*/' { skip(); };

OBRACE: '{' ;
CBRACE: '}' ;
OPARENT: '(';
CPARENT: ')' ;
SEMI: ';' ;
COMMA: ',' ;

PRINTLNX: 'printlnx';
PRINTLN: 'println';
PRINTX: 'printx';
PRINT: 'print';

TRUE: 'true';
FALSE: 'false';
THIS: 'this';
WHILE: 'while';
IF: 'if';
ELSE: 'else';




IDENT: ( LETTER
		| '$'
		| '_'
		)
		( LETTER
		| DIGIT
		| '$'
		| '_'
		)*;

FLOAT: FLOATHEX | FLOATDEC;
INT: DIGIT+;

NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
EQEQ: '==';
EQUALS: '=';
GT: '>';
LT: '<';


PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';

AND: '&&';
OR: '||';

EXCLAM: '!';



STRING: '"' (STRING_CAR | SLAS | SLASS)*? '"' {setText(getText().substring(getText().indexOf('"')+1, getText().lastIndexOf('"')));};

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;


 // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
