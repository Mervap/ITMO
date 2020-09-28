{-# LANGUAGE InstanceSigs #-}

module Block3.Task2
  ( NonEmpty(..)
  , fromList
  , ThisOrThat(..)
  ) where

import NonEmpty

fromList :: [a] -> NonEmpty a
fromList (a:as) = a :| as
fromList []     = error "Empty list"

data ThisOrThat a b
  = This a
  | That b
  | Both a b
  deriving (Show, Eq)

instance (Semigroup a, Semigroup b) => Semigroup (ThisOrThat a b) where
  (<>) :: ThisOrThat a b -> ThisOrThat a b -> ThisOrThat a b
  This x <> This x1 = This (x <> x1)
  That y <> That y1 = That (y <> y1)
  Both x y <> This x1 = Both (x <> x1) y
  Both x y <> That y1 = Both x (y <> y1)
  Both x y <> Both x1 y1 = Both (x <> x1) (y <> y1)
  This x1 <> Both x y = Both (x1 <> x) y
  That y1 <> Both x y = Both x (y1 <> y)
  This x <> That y = Both x y
  That y <> This x = Both x y
