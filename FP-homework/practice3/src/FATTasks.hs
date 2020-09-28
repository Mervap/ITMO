{-# LANGUAGE InstanceSigs #-}
module FATTasks where

import Data.Foldable

-- Task 6
data Point3D a = Point3D a a a
  deriving Show

instance Functor Point3D where
  fmap f (Point3D x y z) = Point3D (f x) (f y) (f z)
  
instance Applicative Point3D where
  pure x = Point3D x x x
  Point3D fx fy fz <*> Point3D x y z = Point3D (fx x) (fy y) (fz z)
  
-- Task 7
data Tree a 
  = Leaf (Maybe a) 
  | Branch (Tree a) (Maybe a) (Tree a) 
  deriving Show
  
instance Functor Tree where 
  fmap f (Leaf m) = Leaf $ fmap f m
  fmap f (Branch l m r) = Branch (fmap f l) (fmap f m) (fmap f r)
  
-- Leaf m <*> pure n = pure ($ n) <*> Leaf m
-- a = Branch (Branch (Leaf $ Just (+ 2)) Nothing (Leaf $ Just (* 5))) (Just (\x -> x - 5)) (Leaf $ Just (** 4))
-- b = Branch (Branch (Leaf $ Just 2) Nothing (Leaf $ Just 5)) (Just 10) (Leaf $ Just 4)
instance Applicative Tree where 
  pure = Leaf . Just
  
  Leaf mf <*> Leaf mx = Leaf $ mf <*> mx
  f@(Leaf mf) <*> Branch lx mx rx = Branch (f <*> lx) (mf <*> mx) (f <*> rx)
  Branch lf mf rf <*> Branch lx mx rx = Branch (lf <*> lx) (mf <*> mx) (rf <*> rx)
  Branch lf mf rf <*> x@(Leaf mx) = Branch (lf <*> x) (mf <*> mx) (rf <*> x)
  
-- Task 8
(<$) :: Functor f => a -> f b -> f a
(<$) = fmap . const

(*>) :: Applicative f => f a -> f b -> f b
a1 *> a2 = (id Prelude.<$ a1) <*> a2

-- Task 9
instance Foldable Tree where

instance Traversable Tree where 
  traverse :: Applicative f => (a -> f b) -> Tree a -> f (Tree b)
  traverse f (Leaf x) = Leaf <$> traverse f x
  traverse f (Branch l x r ) = Branch <$> traverse f l <*> traverse f x <*> traverse f r
  
-- Task 10 

traverseF :: (Traversable t, Applicative f) => (a -> f b) -> t a -> f (t b)
traverseF f = sequenceA . fmap f

-- Task 11
sequenceAF :: (Traversable t, Applicative f) => t (f a) -> f (t a)
sequenceAF = traverse id
 
