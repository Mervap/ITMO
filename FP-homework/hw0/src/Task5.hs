module Task5
  (
    -- * Type of Church numeral.
    Nat

    -- * Operations with Church numerals.
  , zero
  , succChurch
  , churchPlus
  , churchMult
  , churchToInt
  ) where

-- | Church numeral type.
type Nat a = (a -> a) -> a -> a

-- | Zero number.
zero :: Nat a
zero _ x = x

-- | Operation of adding a one to Church numeral.
succChurch :: Nat a -> Nat a
succChurch n f x = f (n f x)

-- | Operation of adding two Church numerals.
churchPlus :: Nat a -> Nat a -> Nat a
churchPlus a b f x = a f (b f x)

-- | Operation of multiplying two Church numerals.
churchMult :: Nat a -> Nat a -> Nat a
churchMult a b = a . b

-- | Converting 'Nat' to 'Integer'.
churchToInt :: Nat Integer -> Integer
churchToInt n = n (+ 1) 0
