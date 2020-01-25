{
module Parser where

import Types
import Lexer
}

%name      parser
%tokentype { Token }
%error     { parseError }

%token
 '('        { OpenBracketToken }
 ')'        { CloseBracketToken }
 '\\'       { SlashToken }
 '\.'       { DotToken }
 VARIABLE   { VariableToken $$ }

%left VARIABLE
%%

Expr
    : Abstraction                { $1 }
    | Application                { $1 }

Abstraction
    : '\\' VARIABLE '\.' Expr    { Abstraction $2 $4 }

Application
    : Application Atom           { Application $1 $2 }
    | Atom                       { $1 }

Atom
    : '(' Expr ')'               { $2 }
    | Abstraction                { $1 }
    | VARIABLE                   { Var $1 }

{
parseError = error "Parse error"
}