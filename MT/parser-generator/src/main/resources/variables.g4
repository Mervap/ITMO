variables ;

root returns[Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>> = mutableListOf<Pair<String, Int>>() to mutableMapOf<String, Int>()]
    : assignment[$res] SCOL
      root_[$res]
    ;

root_[context : Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>]
    : assignment[context] SCOL
      root_[context]
    | EMPTY
    ;

assignment[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>] returns [Int]
    : IDENTIFIER EQ expression[context]
        {
            val pair = $IDENTIFIER0.text to $expression2.res as Int
            context.second.put(pair.first, pair.second)
            context.first.add(pair)
            $res = $expression2.res as Int
        }
     ;

expression[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>] returns[Int]
    : term[context] expression1[context, $term0.res as Int] { $res = $expression11.res as Int }
    ;

expression1[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, acc: Int] returns[Int]
    : PLUS term[context] expression1[context, acc + $term1.res as Int] { $res = $expression12.res as Int }
    | MINUS term[context] expression1[context, acc - $term1.res as Int] { $res = $expression12.res as Int }
    | EMPTY { $res = acc }
    ;

term[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>] returns[Int]
    : factor[context] term1[context, $factor0.res as Int] { $res = $term11.res as Int } ;

term1[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, acc: Int] returns[Int]
    : MUL factor[context] term1[context, acc * $factor1.res as Int] { $res = $term12.res as Int }
    | DIV factor[context] term1[context, acc / $factor1.res as Int] { $res = $term12.res as Int }
    | EMPTY { $res = acc }
    ;

factor[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>] returns[Int]
    : NUMBER { $res = $NUMBER0.text.toInt() }
    | MINUS expression[context] { $res = -($expression1.res as Int) }
    | LP expression[context] RP { $res = $expression1.res as Int }
    | IDENTIFIER identifier_contion[context, $IDENTIFIER0.text!!] { $res = $identifier_contion1.res as Int }
    ;

identifier_contion[context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, name: String] returns[Int]
    : EQ expression[context]
         {
             val pair = name to $expression1.res as Int
             context.second.put(pair.first, pair.second)
             context.first.add(pair)
             $res = $expression1.res as Int
         }
    | EMPTY { res = context.second.get(name)!! }
    ;


WHITESPACES : '[ \n\t\r]' skip ;

SCOL : ';' ;
LP : '\(' ;
RP : '\)' ;
EQ : '=' ;
PLUS : '\+' ;
MINUS : '-' ;
MUL : '\*' ;
DIV : '/' ;
NUMBER : '[0-9]+' ;
IDENTIFIER : '[a-zA-Z_]([a-zA-Z_]|[0-9])*' ;
