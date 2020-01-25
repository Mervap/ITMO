{
module Lexer where
}

%wrapper "basic"

$letters      = [A-Z]
$variableChar = [A-Z0-9']
@whites       = $white+
@isVariable   = $letters $variableChar*

tokens :-

  @whites       ;
  \,            { \_ -> CommaToken}
  "|-"          { \_ -> TurnstileToken}
  \(            { \_ -> OpenBracketToken }
  \)            { \_ -> CloseBracketToken }
  "->"          { \_ -> ImplToken }
  \|            { \_ -> OrToken }
  \&            { \_ -> AndToken }
  \!            { \_ -> NotToken }
  @isVariable   { \var -> VariableToken var }

{

data Token = CommaToken
              | TurnstileToken
              | OpenBracketToken
              | CloseBracketToken
              | ImplToken
              | OrToken
              | AndToken
              | NotToken
              | VariableToken String
           deriving (Show, Eq)

}