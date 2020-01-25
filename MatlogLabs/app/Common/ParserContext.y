{
module ParserContext where

import Types
import Lexer
}

%name      parserC
%tokentype { Token }
%error     { parseError }

%token
 '|-'      { TurnstileToken }
 ','       { CommaToken }
 '('       { OpenBracketToken }
 ')'       { CloseBracketToken }
 '->'      { ImplToken }
 '|'       { OrToken }
 '&'       { AndToken }
 '!'       { NotToken }
 VARIABLE  { VariableToken $$ }

%left '|-'
%right ','
%right '->'
%left '|'
%left '&'
%left '!'
%%

LastExpr
    : Context '|-' Expr   { ($1, $3) }
    | '|-' Expr           { ([], $2) }

Context
    : Expr ',' Context    { $1 : $3 }
    | Expr                { [$1] }

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