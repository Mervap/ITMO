module Task4 where

import Control.Monad.Writer
import Data.Tuple (swap)

gcdWithLog :: (Show a, Integral a) => a -> a -> ([(a, a)], a)
gcdWithLog x y = swap $ runWriter $ gcdWithLogW x y
  where
    gcdWithLogW :: (Show a, Integral a) => a -> a -> Writer [(a, a)] a
    gcdWithLogW a 0 = writer (a, [(a, 0)])
    gcdWithLogW a b = gcdWithLogW b (a `mod` b) >>= \v -> writer (v, [(a, b)])