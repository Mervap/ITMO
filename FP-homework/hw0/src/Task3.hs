module Task3
  (
    -- * Expressed BCW and I combinators by SK combinators.
    composition
  , identity
  , contraction
  , permutation
  ) where

-- | S combinator.
s :: (a -> b -> c) -> (a -> b) -> a -> c
s f g x = f x (g x)

-- | B combinator. Works like ('.').
composition :: (b -> c) -> (a -> b) -> a -> c
composition = s (const s) const

-- | I combinator. Works like 'id'.
identity :: a -> a
identity = s const const

-- | W combinator. Ideologically applies the argument to the function twice.
contraction :: (a -> a -> b) -> a -> b
contraction = s s (s const)

-- | C combinator. Works like 'flip'.
permutation :: (a -> b -> c) -> b -> a -> c
permutation = s (s (const (s (const s) const)) s) (const const)
