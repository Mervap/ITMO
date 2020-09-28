{-# LANGUAGE InstanceSigs #-}

module Block4.Task2
  ( Tree(..)
  ) where

data Tree a
  = Branch (Tree a) (Tree a)
  | Leaf a
  deriving (Show, Eq)

instance Functor Tree where
  fmap f (Leaf m)     = Leaf $ f m
  fmap f (Branch l r) = Branch (fmap f l) (fmap f r)

instance Applicative Tree where
  pure = Leaf
  Leaf mf <*> Leaf mx = Leaf $ mf mx
  f@(Leaf _) <*> Branch lx rx = Branch (f <*> lx) (f <*> rx)
  Branch lf rf <*> x@(Leaf _) = Branch (lf <*> x) (rf <*> x)
  Branch lf rf <*> Branch lx rx = Branch (lf <*> lx) (rf <*> rx)

instance Foldable Tree where
  foldr :: (a -> b -> b) -> b -> Tree a -> b
  foldr f def (Leaf v)     = f v def
  foldr f def (Branch l r) = foldr f (foldr f def r) l

instance Traversable Tree where
  traverse :: Applicative f => (a -> f b) -> Tree a -> f (Tree b)
  traverse f (Leaf x)     = Leaf <$> f x
  traverse f (Branch l r) = Branch <$> traverse f l <*> traverse f r
