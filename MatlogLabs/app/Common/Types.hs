module Types where

data BinOp
  = And
  | Or
  | Impl
  deriving (Eq, Ord)

data Expr
  = Op BinOp
       Expr
       Expr
  | Not Expr
  | Var String
  deriving (Ord)

data AnnotatedExpr
  = Ax Int
       Expr
  | Hyp Int
        Expr
  | MP Int
       Int
       Expr
  deriving (Show)

instance Show BinOp where
  show And  = "&"
  show Or   = "|"
  show Impl = "->"

instance Show Expr where
  show (Op op a b) = "(" ++ show a ++ " " ++ show op ++ " " ++ show b ++ ")"
  show (Not e)     = "!" ++ show e
  show (Var name)  = name

instance Eq Expr where
  (Op op a b) == (Op op1 c d) = (op == op1) && (a == c) && (b == d)
  (Not e) == (Not e1) = e == e1
  (Var name) == (Var name1) = name == name1
  _ == _ = False
