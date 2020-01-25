calculator ;

e returns[Int] : t e1[$t0.res as Int] { $res = $e11.res as Int } ;

e1[acc: Int] returns[Int]
    : PLUS t e1[acc + $t1.res as Int] { $res = $e12.res as Int }
    | MINUS t e1[acc - $t1.res as Int] { $res = $e12.res as Int }
    | EMPTY { $res = acc }
    ;

t returns[Int] : f t1[$f0.res as Int] { $res = $t11.res as Int } ;

t1 [acc: Int] returns[Int]
    : MUL f t1[acc * $f1.res as Int] { $res = $t12.res as Int }
    | DIV f t1[acc / $f1.res as Int] { $res = $t12.res as Int }
    | EMPTY { $res = acc }
    ;

f returns[Int]
    : NUMBER { $res = $NUMBER0.text.toInt() }
    | MINUS f { $res = -($f1.res as Int) }
    | LP e RP { $res = $e1.res as Int };

WS : '[ \n\t\r]+' skip ;

NUMBER : '[0-9]+' ;
PLUS : '\+';
MINUS : '-';
MUL : '\*';
DIV : '/';
LP : '\(';
RP : '\)';
