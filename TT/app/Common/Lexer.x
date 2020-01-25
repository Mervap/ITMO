{
module Lexer where
}

%wrapper "basic"

$letters      = [a-z]
$variableChar = [a-z0-9']
@whites       = $white+
@isVariable   = $letters $variableChar*

tokens :-

  @whites       ;
  \\            { \_ -> SlashToken }
  \.            { \_ ->  DotToken}
  \(            { \_ -> OpenBracketToken }
  \)            { \_ -> CloseBracketToken }
  @isVariable   { \var -> VariableToken var }

{

data Token = SlashToken
              | DotToken
              | OpenBracketToken
              | CloseBracketToken
              | VariableToken String
           deriving (Show, Eq)

}