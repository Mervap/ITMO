{-# LANGUAGE InstanceSigs #-}

module Block1.Task2
  ( 
    -- * The type of natural numbers
    Nat(..)
    
    -- * Conversion @Nat@ to @Integer@
  , toIntegerNat
  
    -- * Some arithmetic operations with @Nat@
  , evenNat
  , divNat
  , modNat
  ) where

data Nat
  = Z
  | S Nat
  deriving (Show)

instance Num Nat where
  (+) :: Nat -> Nat -> Nat
  a + Z = a
  Z + b = b
  S a + b = S (a + b)
  
  (*) :: Nat -> Nat -> Nat
  _ * Z = Z
  Z * _ = Z
  S a * b = (a * b) + b
  
  fromInteger :: Integer -> Nat
  fromInteger x
    | x < 0     = error "Natural numbers can't be negative"
    | x == 0    = Z
    | otherwise = S (fromInteger (x - 1))
  
  abs :: Nat -> Nat
  abs = id
  
  signum :: Nat -> Nat
  signum Z = Z
  signum _ = S Z
  
  negate :: Nat -> Nat
  negate = error "Unsopported"
  
  (-) :: Nat -> Nat -> Nat
  Z - _ = Z
  x - Z = x
  (S a) - (S b) = a - b

instance Eq Nat where
  (==) :: Nat -> Nat -> Bool
  Z == Z = True
  (S x) == (S y) = x == y
  _ == _ = False

instance Ord Nat where
  compare :: Nat -> Nat -> Ordering
  compare Z Z         = EQ
  compare Z _         = LT
  compare _ Z         = GT
  compare (S x) (S y) = compare x y

toIntegerNat :: Nat -> Integer
toIntegerNat Z     = 0
toIntegerNat (S x) = toIntegerNat x + 1

evenNat :: Nat -> Bool
evenNat Z     = True
evenNat (S x) = not $ evenNat x

divNat :: Nat -> Nat -> Nat
divNat _ Z = error "Division by zero"
divNat a b
  | a < b     = Z
  | otherwise = S ((a - b) `divNat` b)

modNat :: Nat -> Nat -> Nat
modNat _ Z = error "Division by zero"
modNat a b
  | a < b     = a
  | otherwise = (a - b) `modNat` b
