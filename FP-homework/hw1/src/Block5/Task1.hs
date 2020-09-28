module Block5.Task1
  ( Expr(..)
  , ArithmeticError(..)
  , evalExpr
  ) where

import Control.Monad

data Expr
  = Const Int
  | Add Expr Expr
  | Sub Expr Expr
  | Mul Expr Expr
  | Div Expr Expr
  | Pow Expr Expr
  deriving (Show, Eq)

data ArithmeticError
  = DivisionByZero
  | NegativePower
  deriving (Show, Eq)

evalExpr :: Expr -> Either ArithmeticError Int
evalExpr (Const x) = return x
evalExpr (Add l r) = liftEval (+) l r
evalExpr (Sub l r) = liftEval (-) l r
evalExpr (Mul l r) = liftEval (*) l r
evalExpr (Div l r) = evalExpr r >>= throwErrorIfNeeded (== 0) DivisionByZero >>= liftEval div l
evalExpr (Pow l r) = evalExpr r >>= throwErrorIfNeeded (< 0) NegativePower >>= liftEval (^) l

throwErrorIfNeeded :: (Int -> Bool) -> ArithmeticError -> Int -> Either ArithmeticError Expr
throwErrorIfNeeded fun err x =
  if fun x
    then Left err
    else Right $ Const x

liftEval :: (Int -> Int -> Int) -> Expr -> Expr -> Either ArithmeticError Int
liftEval fun l r = liftM2 fun (evalExpr l) (evalExpr r)
