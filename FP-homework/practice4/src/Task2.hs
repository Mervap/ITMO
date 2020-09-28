module Task2 where

import Data.Map
import Data.Maybe
import Control.Monad.Reader

data Expr a
  = Const a
  | Var String
  | Add (Expr a) (Expr a)
  | Sub (Expr a) (Expr a)
  | Mul (Expr a) (Expr a)

type VarMap a = Map String a

evalExpr :: Num a => Expr a -> VarMap a -> a
evalExpr e = runReader (evalExprWithReader e)
  where
    evalExprWithReader :: Num a => Expr a -> Reader (VarMap a) a
    evalExprWithReader (Const x) = return x
    evalExprWithReader (Var s) = asks (\mp -> fromMaybe (error $ "No variable " ++ s) (mp !? s))
    evalExprWithReader (Add l r) = evalHelper (+) l r
    evalExprWithReader (Sub l r) = evalHelper (-) l r
    evalExprWithReader (Mul l r) = evalHelper (*) l r
    --  --
    evalHelper :: Num a => (a -> a -> a) -> Expr a -> Expr a -> Reader (VarMap a) a
    evalHelper f l r = liftM2 f (evalExprWithReader l) (evalExprWithReader r)