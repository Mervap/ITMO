module Types where

import           Data.Set as Set

data Pair =
  Pair Expr Int

data Triple =
  Triple Expr Bool Int

data Expr
  = Abstraction String Expr
  | Application Expr Expr
  | Var String
  | Wrap Int
  deriving (Ord)

data Eqq = Eqq Type Type deriving(Ord)

data Type 
  = Impl Type Type 
  | AtomVar Int
  deriving(Ord)

data TypedExpr
  = TAbstraction String TypedExpr Type 
  | TApplication TypedExpr TypedExpr Type 
  | TVar String Type 
  deriving(Ord)

instance Show Expr where
  show (Abstraction var expr) = "(\\" ++ var ++ "." ++ show expr ++ ")"
  show (Application e1 e2)    = "(" ++ show e1 ++ " " ++ show e2 ++ ")"
  show (Var name)             = name
  show (Wrap i)               = "W(" ++ show i ++ ")"

instance Eq Expr where
  (Abstraction var1 expr1) == (Abstraction var2 expr2) = (var1 == var2) && (expr1 == expr2)
  (Application e11 e12) == (Application e21 e22)       = (e11 == e21) && (e12 == e22)
  (Var name) == (Var name1)                            = name == name1
  (Wrap i) == (Wrap j)                                 = i == j
  _ == _ = False
  
instance Eq Type where
  (Impl t1 t2) == (Impl t3 t4) = (t1 == t3) && (t2 == t4)
  (AtomVar i1) == (AtomVar i2) = i1 == i2 
  _ == _ = False

instance Eq TypedExpr where 
  (TAbstraction var1 expr1 t1) == (TAbstraction var2 expr2 t2) = (var1 == var2) && (expr1 == expr2) && (t1 == t2)
  (TApplication e11 e12 t1) == (TApplication e21 e22 t2)       = (e11 == e21) && (e12 == e22) && (t1 == t2)
  (TVar name t1) == (TVar name1 t2)                            = (name == name1) && (t1 == t2) 
  _ == _ = False
  
instance Eq Eqq where 
  (Eqq t1 t2) == (Eqq t3 t4) = (t1 == t3) && (t2 == t4)
  
instance Show Type where 
  show (Impl t1 t2) = "(" ++ show t1 ++ " -> " ++ show t2 ++ ")"
  show (AtomVar i) = "t" ++ show i 
  
instance Show TypedExpr where 
  show (TAbstraction var expr t) = "(\\" ++ var ++ "." ++ show expr ++ ")"
  show (TApplication e1 e2 t)    = "(" ++ show e1 ++ " " ++ show e2 ++ ")"
  show (TVar name t)             = name 

instance Show Eqq where 
  show (Eqq t1 t2) = show t1 ++ " = " ++ show t2
