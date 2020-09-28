{-# LANGUAGE InstanceSigs     #-}
{-# LANGUAGE LambdaCase       #-}

module Block6.Task2 where

import Block6.Task1

ok :: Monoid a => Parser s a
ok = Parser $ \s -> Just (mempty, s)

eof :: Monoid s => Parser s ()
eof =
  Parser $ \case
    [] -> Just ((), mempty)
    _ -> Nothing

satisfy :: (s -> Bool) -> Parser s s
satisfy p =
  Parser $ \case
    [] -> Nothing
    (x:xs) ->
      if p x
        then Just (x, xs)
        else Nothing

element :: Eq s => s -> Parser s s
element c = satisfy (== c)

stream :: Eq a => [a] -> Parser a [a]
stream [] = Parser $ \s -> Just ([], s)
stream (x:xs) =
  Parser $ \s -> do
    (t, r) <- runParser (element x) s
    (t1, r1) <- runParser (stream xs) r
    return (t : t1, r1)
