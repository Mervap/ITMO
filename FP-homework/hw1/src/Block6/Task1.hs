{-# LANGUAGE InstanceSigs #-}

module Block6.Task1 where

import Control.Applicative
import Control.Monad

newtype Parser s a =
  Parser
    { runParser :: [s] -> Maybe (a, [s])
    }

--Functor, Applicative, Monad и Alternative
instance Functor (Parser s) where
  fmap :: (a -> b) -> Parser s a -> Parser s b
  fmap f (Parser parser) = Parser (fmap (\(a, b) -> (f a, b)) . parser)

instance Applicative (Parser s) where
  pure :: a -> Parser s a
  pure a = Parser $ \s -> Just (a, s)
  (<*>) :: Parser s (a -> b) -> Parser s a -> Parser s b
  Parser pf <*> Parser pa = Parser (pf >=> (\(f, t) -> pa t >>= \(a, r) -> Just (f a, r)))

instance Monad (Parser s) where
  Parser pa >>= f = Parser (pa >=> (\(a, r) -> runParser (f a) r))

instance Alternative (Parser s) where
  empty :: Parser s a
  empty = Parser $ const Nothing
  (<|>) :: Parser s a -> Parser s a -> Parser s a
  Parser pa <|> Parser pb =
    Parser $ \s ->
      case pa s of
        Nothing -> pb s
        res     -> res
