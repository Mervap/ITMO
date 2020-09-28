module Block5.Task2
  ( moving
  ) where

import Control.Monad.State
import Data.List

moving :: (Real a, Fractional b) => Int -> [a] -> [b]
moving cnt list = evalState (helper list) []
  where
    helper :: (Real a, Fractional b) => [a] -> State [a] [b]
    helper [] = return []
    helper (x:xs) = do
      st <- get
      let newSt = take cnt (x : st)
      return $ mean newSt : evalState (helper xs) newSt
      --
    mean :: (Real a, Fractional b) => [a] -> b
    mean [] = 0
    mean list  = realToFrac (sum list) / genericLength list
