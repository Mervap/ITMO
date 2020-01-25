infixToPostfix ;

e returns[String] : t e1[$t0.res as String] { $res = $e11.res as String } ;

e1[acc: String] returns[String]
    : PLUS t e1[acc + " " + $t1.res as String + " +"] { $res = $e12.res as String }
    | MINUS t e1[acc + " " + $t1.res as String + " -"] { $res = $e12.res as String }
    | EMPTY { $res = acc }
    ;

t returns[String] : f t1[$f0.res as String] { $res = $t11.res as String } ;

t1 [acc: String] returns[String]
    : MUL f t1[acc + " " + $f1.res as String + " *"] { $res = $t12.res as String }
    | DIV f t1[acc + " " +$f1.res as String + " /"] { $res = $t12.res as String }
    | EMPTY { $res = acc }
    ;

f returns[String]
    : NUMBER { $res = $NUMBER0.text }
    | LP e RP { $res = $e1.res as String };

WS : '[ \n\t\r]+' skip ;

NUMBER : '[0-9]+' ;
PLUS : '\+';
MINUS : '-';
MUL : '\*';
DIV : '/';
LP : '\(';
RP : '\)';
