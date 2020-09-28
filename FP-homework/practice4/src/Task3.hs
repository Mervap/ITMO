module Task3 where

import Control.Monad.Writer

data Expr a
  = Const a
  | Add (Expr a) (Expr a)
  | Sub (Expr a) (Expr a)
  | Mul (Expr a) (Expr a)

evalExpr :: (Show a, Num a) => Expr a -> a
evalExpr (Const x) = x
evalExpr (Add l r) = evalExpr l + evalExpr r
evalExpr (Sub l r) = evalExpr l - evalExpr r
evalExpr (Mul l r) = evalExpr l * evalExpr r

data Statement a
  = Seq (Statement a) (Statement a)
  | EvalAndPrint (Expr a)
  | Log String

evalStatement :: (Show a, Integral a) => Statement a -> String
evalStatement = execWriter . evalStatementWithWriter
  where
    evalStatementWithWriter :: (Show a, Integral a) => Statement a -> Writer String ()
    evalStatementWithWriter (Seq l r) = lResult >>= const rResult
      where
        lResult = evalStatementWithWriter l
        rResult = evalStatementWithWriter r
    evalStatementWithWriter (EvalAndPrint e) = tell $ show (evalExpr e) ++ "; "
    evalStatementWithWriter (Log s) = tell $ s ++ "; "