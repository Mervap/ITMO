{-# LANGUAGE InstanceSigs #-}
module NonEmpty where

data NonEmpty a =
  a :| [a]
  deriving (Show, Eq)
  
instance Semigroup (NonEmpty a) where
  (<>) :: NonEmpty a -> NonEmpty a -> NonEmpty a
  (x :| xs) <> (y :| ys) = x :| (xs ++ (y : ys))
  
-- Block 4. Task 3.  
  
instance Functor NonEmpty where
  fmap f (h :| hs) = f h :| fmap f hs

instance Applicative NonEmpty where
  pure = (:| [])
  (a :| as) <*> (b :| bs) = x :| xs
    where
      (x:xs) = (a : as) <*> (b : bs)

instance Monad NonEmpty where
  (a :| as) >>= f = b :| (bs ++ t)
    where
      toList :: NonEmpty a -> [a]
      toList (x :| xs) = x : xs
      (b :| bs) = f a
      t = as >>= (toList . f)

instance Foldable NonEmpty where
  foldr :: (a -> b -> b) -> b -> NonEmpty a -> b
  foldr f def (h :| hs) = f h (foldr f def hs)

instance Traversable NonEmpty where
  traverse :: Applicative f => (a -> f b) -> NonEmpty a -> f (NonEmpty b)
  traverse f (h :| hs) = (:|) <$> f h <*> traverse f hs
