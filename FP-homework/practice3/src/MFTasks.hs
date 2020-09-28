{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE InstanceSigs      #-}

module MFTasks where

import Data.Foldable (foldr')

-- Task 1
newtype Last a =
  Last (Maybe a)

instance Semigroup a => Semigroup (Last a) where
  (<>) :: Last a -> Last a -> Last a
  (Last x) <> (Last y) = Last (x <> y)

-- Task 2
mconcatF :: Monoid m => [m] -> m
mconcatF = foldr' mappend mempty

-- Task 3
foldMapF :: (Foldable f, Monoid m) => (a -> m) -> f a -> m
foldMapF f = foldr (mappend . f) mempty

-- Task 4
newtype Kek a =
  Kek [a]

instance Foldable Kek where
  foldMap = foldMapF

-- Task 5
newtype Composition a =
  Composition (a -> a)

unWrap :: Composition a -> (a -> a)
unWrap (Composition f) = f

instance Semigroup (Composition a) where
  Composition fx <> Composition fy = Composition (fx . fy)

instance Monoid (Composition a) where
  mempty = Composition id

foldrF :: Foldable t => (a -> b -> b) -> b -> t a -> b
foldrF f s l = (unWrap $ foldMap (Composition . f) l) s
