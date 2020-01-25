pascal_logic ;

e : x e1 ;
e1 : OR x e1 | EMPTY ;
x : c x1 ;
x1 : XOR c x1 | EMPTY ;
c : n c1 ;
c1 : AND n c1 | EMPTY ;
n : NOT v | v ;
v : VARIABLE | b | LP e RP ;
b : BOOL ;

WS : '[ \n\t\r]+' skip ;

LP : '\(' ;
RP : '\)' ;
OR : 'or' ;
XOR : 'xor' ;
AND : 'and' ;
NOT : 'not' ;
BOOL : 'true' | 'false' ;
VARIABLE : '[a-zA-Z][a-zA-Z0-9]*' ;