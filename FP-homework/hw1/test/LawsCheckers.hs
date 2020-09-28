{-# LANGUAGE AllowAmbiguousTypes #-}
{-# LANGUAGE ScopedTypeVariables #-}

module LawsCheckers where

import Hedgehog
import qualified Hedgehog.Gen as Gen
import Test.Tasty.HUnit

functorIdLaw :: (Show (a Int), Eq (a Int), Functor a) => Gen (a Int) -> Property
functorIdLaw generator =
  property $ do
    tree <- forAll generator
    fmap id tree === tree

functorCompositionLaw :: (Show (a Int), Eq (a Int), Functor a) => Gen (a Int) -> Property
functorCompositionLaw generator =
  property $ do
    tree <- forAll generator
    fmap ((* 10) . (+ 2)) tree === fmap (* 10) (fmap (+ 2) tree)

applicativeIdentityLaw :: (Show (a Int), Eq (a Int), Applicative a) => Gen (a Int) -> Property
applicativeIdentityLaw generator =
  property $ do
    v <- forAll generator
    (pure id <*> v) === v

applicativeCompositionLaw :: (Show (a Int), Eq (a Int), Applicative a) => Gen (a Int) -> Property
applicativeCompositionLaw generator =
  property $ do
    uVal <- forAll generator
    vVal <- forAll generator
    w <- forAll generator
    let u = fmap (+) uVal
    let v = fmap (*) vVal
    (pure (.) <*> u <*> v <*> w) === (u <*> (v <*> w))

applicativeHomomorphismLaw ::
     forall a. (Show (a Int), Eq (a Int), Applicative a)
  => Property
applicativeHomomorphismLaw =
  property $ do
    val <- forAll $ Gen.enum (-100) 100
    x <- forAll $ Gen.enum (-100) 100
    let pureF = (pure :: (Int -> Int) -> a (Int -> Int)) (+ val)
    (pureF <*> pure x) === pure (x + val)

applicativeInterchangeLaw :: (Show (a Int), Eq (a Int), Applicative a) => Gen (a Int) -> Property
applicativeInterchangeLaw generator =
  property $ do
    uVal <- forAll generator
    val <- forAll $ Gen.enum (-100) 100
    let u = fmap (*) uVal
    (u <*> pure val) === (pure ($ val) <*> u)

foldableFoldMap :: (Show (a Int), Eq (a Int), Foldable a) => Gen (a Int) -> Property
foldableFoldMap generator =
  property $ do
    t <- forAll generator
    foldMap (: []) t === foldr (mappend . (: [])) mempty t

newtype Composition a =
  Composition (a -> a)

unWrap :: Composition a -> (a -> a)
unWrap (Composition f) = f

instance Semigroup (Composition a) where
  Composition fx <> Composition fy = Composition (fx . fy)

instance Monoid (Composition a) where
  mempty = Composition id

foldableFoldr :: (Show (a Int), Eq (a Int), Foldable a) => Gen (a Int) -> Property
foldableFoldr generator =
  property $ do
    t <- forAll generator
    foldr (:) [] t === (unWrap $ foldMap (Composition . (:)) t) []
