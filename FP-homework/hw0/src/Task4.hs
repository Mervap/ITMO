module Task4
  (
    -- * Examples of using fixed point operator.
    iterateElement
  , fibonacci
  , factorial
  , mapFix
  ) where

import Data.Function

-- | Generate infinity sequence like [x, x, ...].
iterateElement :: a -> [a]
iterateElement = fix . (:)

-- | Calculates the nth Fibonacci number. Defined only for non-negative numbers.
--   Otherwise throws an error.
fibonacci :: Integer -> Integer
fibonacci n
  | n < 0 = error "Only non-negative members of the sequence allowed"
  | otherwise = fix fibonacciInner n
  where
    fibonacciInner :: (Integer -> Integer) -> Integer -> Integer
    fibonacciInner f x
      | x < 2 = x
      | otherwise = f (x - 1) + f (x - 2)

-- | Calculates the factorial of the number n. Defined only for non-negative
--   numbers. Otherwise throws an error.
factorial :: Integer -> Integer
factorial n
  | n < 0 = error "Factorial is defined on non-negative numbers"
  | otherwise = fix fibonacciInner n
  where
    fibonacciInner :: (Integer -> Integer) -> Integer -> Integer
    fibonacciInner f x
      | x < 2 = 1
      | otherwise = x * f (x - 1)

-- | Map list values by function. Works like 'map'.
mapFix :: (a -> b) -> [a] -> [b]
mapFix = fix mapFixInner
  where
    mapFixInner :: ((a -> b) -> [a] -> [b]) -> (a -> b) -> [a] -> [b]
    mapFixInner _ _ []        = []
    mapFixInner f mapF (x:xs) = mapF x : f mapF xs
