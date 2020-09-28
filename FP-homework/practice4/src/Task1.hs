module Task1 where

import Control.Monad

-- Task 1
data Expr a
  = Const a
  | MinusInfinity
  | PlusInfinity
  | Add (Expr a) (Expr a)
  | Sub (Expr a) (Expr a)
  | Mul (Expr a) (Expr a)
  | Div (Expr a) (Expr a)
  | Pow (Expr a) (Expr a)
  
data ErrMsg
  = DivisionByZero
  | NaN
  
evalExpr1 :: Integral a => Expr a -> Either ErrMsg a
evalExpr1 (Const x) = return x
evalExpr1 (Add l r) = evalHelper (+) l r
evalExpr1 (Sub l r) = evalHelper (-) l r
evalExpr1 (Mul l r) = evalHelper (*) l r
evalExpr1 (Div l r) = evalExpr1 r >>= isZero >> evalHelper div l r
  where
    isZero :: Integral a => a -> Either ErrMsg a
    isZero 0 = Left DivisionByZero
    isZero x = Right x
evalExpr1 (Pow l r) = evalHelper (+) l r

evalHelper :: Integral a => (a -> a -> a) -> Expr a -> Expr a -> Either ErrMsg a
evalHelper f l r = liftM2 f (evalExpr1 l) (evalExpr1 r)