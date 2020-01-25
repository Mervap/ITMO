module Main where

import Types
import Lexer (alexScanTokens)
import Parser (parser)

main :: IO ()
main = do
  input <- getLine
  case parser (alexScanTokens input) of
    Left err   -> putStrLn err
    Right expr -> print expr