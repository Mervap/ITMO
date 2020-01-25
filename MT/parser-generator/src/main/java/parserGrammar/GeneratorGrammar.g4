grammar GeneratorGrammar;

// Parser rules

root returns[InputGrammar grammar = new InputGrammar()] :
    (name=(GRAMMAR_NAME | LEXEM | IDENTIFIER) { $grammar.setName($name.text); }) ';'
    (header { $grammar.setHeader($header.code); })?
    (rule_ { $grammar.addRule($rule_._rule); } ';')* ;

header returns[String code] : 'header' CODE { $code = $CODE == null ? "" : $CODE.text.substring(1, $CODE.text.length() - 1); } ;

rule_ returns[Rule _rule = new Rule()] :
    LEXEM { $_rule.setName($LEXEM.text); }
    ':'
     ((STRING SKIPP { $_rule.addVariant(new Lexeme($STRING.text.substring(1, $STRING.text.length() - 1), true)); })
      |
      ((STRING { $_rule.addVariant(new Lexeme($STRING.text.substring(1, $STRING.text.length() - 1), false)); })
       ('|' STRING { $_rule.addVariant(new Lexeme($STRING.text.substring(1, $STRING.text.length() - 1), false)); })*))
    |
    IDENTIFIER { $_rule.setName($IDENTIFIER.text); }
    (PARAM { $_rule.parseParams($PARAM == null ? "" : $PARAM.text.substring(1, $PARAM.text.length() - 1)); })?
    ('returns' PARAM { $_rule.parseReturns($PARAM == null ? "" : $PARAM.text.substring(1, $PARAM.text.length() - 1)); })?
    ':'
    variant { $_rule.addVariant($variant._variant); }
    ('|' variant { $_rule.addVariant($variant._variant); } )*;

variant returns[Variant _variant = new Variant()] :
     EMPTY CODE? { $_variant.addChild("", "", $CODE == null ? "" : $CODE.text.substring(1, $CODE.text.length() - 1)); }
     |
     ((name=IDENTIFIER PARAM?| name=LEXEM) CODE? {
        $_variant.addChild($name.text, $PARAM == null ? "" : $PARAM.text.substring(1, $PARAM.text.length() - 1), $CODE == null ? "" : $CODE.text.substring(1, $CODE.text.length() - 1));
     })+ ;

// Lexer rules

WS : [ \n\t\r] -> skip ;
EMPTY : 'EMPTY' ;
SKIPP : 'skip' ;

fragment LOWER_LETTER : [a-z_] ;
fragment UPPER_LETTER : [A-Z_] ;
fragment DIGIT : [0-9] ;
IDENTIFIER : LOWER_LETTER (LOWER_LETTER | DIGIT | '_')* ;
LEXEM : UPPER_LETTER (UPPER_LETTER | DIGIT | '_')* ;
GRAMMAR_NAME : (UPPER_LETTER | LOWER_LETTER) (UPPER_LETTER | LOWER_LETTER | DIGIT)* ;

STRING : '\'' (~['] | '\\\'')+ '\'' ;
CODE : '{' .*? '}' ;
PARAM : '[' .*? ']' ;
