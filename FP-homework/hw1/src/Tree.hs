{-# LANGUAGE InstanceSigs #-}

module Tree
    -- * Binary tree
  ( Tree(..)
  ) where

import Data.List.NonEmpty

data Tree a
  = List
  | Branch (Tree a) (NonEmpty a) (Tree a)
  deriving (Show)

instance (Eq a) => Eq (Tree a) where
  List == List = True
  (Branch left1 _data1 right1) == (Branch left2 _data2 right2) =
    (_data1 == _data2) && (left1 == left2) && (right1 == right2)
  _ == _ = False


-- Block 2. Task 1.

instance Foldable Tree where
  foldMap :: Monoid m => (a -> m) -> Tree a -> m
  foldMap _ List = mempty
  foldMap f (Branch left _data right) = foldMap f left <> foldMap f _data <> foldMap f right
  foldr :: (a -> b -> b) -> b -> Tree a -> b
  foldr _ def List = def
  foldr f def (Branch left _data right) = foldr f (foldr f (foldr f def right) _data) left
