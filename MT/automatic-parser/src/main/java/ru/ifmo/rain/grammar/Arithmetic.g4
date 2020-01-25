grammar Arithmetic;
@header {
  import java.util.*;
}
@members {
  private Map<String, Integer> context = new HashMap();
  private List<Map.Entry<String, Integer>> assignments = new ArrayList();

  public List<Map.Entry<String, Integer>> getAllAssignments() {
    return assignments;
  }
}

// Parser rules

root : (assignment ';')* ;
assignment returns [int val]
    : IDENTIFIER '=' expression
        {
            Map.Entry<String, Integer> entry = Map.entry($IDENTIFIER.text, $expression.val);
            context.put(entry.getKey(), entry.getValue());
            assignments.add(entry);
            $val = $expression.val;
        } ;

expression returns [int val]
    : IDENTIFIER { $val = context.get($IDENTIFIER.text); }
    | number { $val = $number.val; }
    | MINUS expression { $val = -$expression.val; }
    | assignment { $val = $assignment.val; }
    | <assoc=right> left=expression POW rigth=expression { $val = (int) Math.pow($left.val, $rigth.val); }
    | left=expression op=(MUL | DIV) rigth=expression
        { if ($op.type == MUL)
            $val = $left.val * $rigth.val;
          else
            $val = $left.val / $rigth.val;
        }
    | left=expression op=(PLUS | MINUS) rigth=expression
        { if ($op.type == PLUS)
            $val = $left.val + $rigth.val;
          else
            $val = $left.val - $rigth.val;
        }
    | '(' expression ')' { $val = $expression.val; }
    ;

number returns [int val]
    : NUMBER { $val = Integer.parseInt($NUMBER.text); }
    | MINUS NUMBER { $val = Integer.parseInt('-' + $NUMBER.text); }
    ;

// Lexer rules

WHITESPACES : [ \n\t\r] -> skip ;

PLUS : '+' ;
MINUS : '-' ;
POW : '**';
MUL : '*' ;
DIV : '/' ;
NUMBER : DIGIT+ ;
IDENTIFIER : LETTER (LETTER | DIGIT)* ;
fragment LETTER : [a-zA-Z_] ;
fragment DIGIT : [0-9] ;
