{-# LANGUAGE DeriveAnyClass #-}

module Task1 where

import Control.Monad
import Control.Monad.State
import Control.Monad.Writer (Writer)

data FunBox
  = A Int
  | B Double
  deriving (Eq, Ord, Show, Read, Semigroup) -- what can we derive?

kek :: forall a . State s a -> (a -> State s b) -> State s b
kek oldState f =
  state $ \s ->
    let (res, st) = runState oldState s
     in runState (f res) st

MonadReader