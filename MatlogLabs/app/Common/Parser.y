{
module Parser where

import Types
import Lexer
}

%name      parser
%tokentype { Token }
%error     { parseError }

%token
 '('       { OpenBracketToken }
 ')'       { CloseBracketToken }
 '->'      { ImplToken }
 '|'       { OrToken }
 '&'       { AndToken }
 '!'       { NotToken }
 VARIABLE  { VariableToken $$ }

%right '->'
%left '|'
%left '&'
%left '!'
%%

Expr
    : Expr '->' Expr      { Op Impl $1 $3 }
    | Expr '|' Expr       { Op Or $1 $3 }
    | Expr '&' Expr       { Op And $1 $3 }
    | '!' Expr            { Not $2 }
    | '(' Expr ')'        { $2 }
    | VARIABLE            { Var $1}


{
parseError = error "Parse error"
}