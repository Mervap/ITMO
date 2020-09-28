{-# LANGUAGE LambdaCase    #-}
{-# LANGUAGE TypeOperators #-}

module Task1
  (
    -- * Equivalence type operator.
    type (<->)

    -- * Proofs properties of disjunction/conjunction operation.
  , distributivity
  , associator
  , eitherAssoc
  ) where

-- | Definition of equivalence type operator.
type (<->) a b = (a -> b, b -> a)

-- | Proof of left-distributive disjunction over conjunction.
distributivity :: Either a (b, c) -> (Either a b, Either a c)
distributivity =
  \case
    Left x       -> (Left x, Left x)
    Right (y, z) -> (Right y, Right z)

-- | One-way proof of conjunction associative.
associator :: (a, (b, c)) -> ((a, b), c)
associator (x, (y, z)) = ((x, y), z)

rightToLeftAssoc :: Either a (Either b c) -> Either (Either a b) c
rightToLeftAssoc =
  \case
    Left x          -> Left $ Left x
    Right (Left y)  -> Left $ Right y
    Right (Right z) -> Right z

leftToRightAssoc :: Either (Either a b) c -> Either a (Either b c)
leftToRightAssoc =
  \case
    Left (Left x)  -> Left x
    Left (Right y) -> Right $ Left y
    Right z        -> Right $ Right z

-- | Proof of disjunction associative.
eitherAssoc :: Either a (Either b c) <-> Either (Either a b) c
eitherAssoc = (rightToLeftAssoc, leftToRightAssoc)
