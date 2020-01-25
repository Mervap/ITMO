module Main where

import           Control.Exception
import           Lexer             (alexScanTokens)
import           Parser            (parser)
import           Types

main :: IO ()
main = do
  input <- getContents
  e <- try (evaluate (parser (alexScanTokens input))) :: IO (Either SomeException Expr)
  case e of
    Left err   -> print err
    Right expr -> print expr
