module Task2
  (
    -- * The @Neg@ type.
    Neg

    -- * Intuitionistic logic statements.
  , doubleNeg
  , excludedNeg
  , pierce
  , doubleNegElim
  , thirdNegElim
  ) where

import Data.Void (Void)

type Neg a = a -> Void

-- | Ninth axiom of logic calculus.
ax9 :: (a -> b) -> (a -> Neg b) -> Neg a
ax9 f g x = g x (f x)

-- | Proof of double negation hanging.
doubleNeg :: a -> Neg (Neg a)
doubleNeg x = ax9 (const x) id

-- | Proof of double negation of law of excluded middle.
excludedNeg :: Neg (Neg (Either a (Neg a)))
excludedNeg = ax9 (. Left) (. Right)

-- | Peirce's law doesn't hold in intuitionistic logic.
pierce :: ((a -> b) -> a) -> a
pierce = undefined

-- | Tenth axiom of classical logic doesn't hold in intuitionistic logic.
doubleNegElim :: Neg (Neg a) -> a
doubleNegElim = undefined

counterposition :: (a -> b) -> Neg b -> Neg a
counterposition f = ax9 f . const

-- | Easy proof of removing the double negation of the three.
thirdNegElim :: Neg (Neg (Neg a)) -> Neg a
thirdNegElim = counterposition doubleNeg
