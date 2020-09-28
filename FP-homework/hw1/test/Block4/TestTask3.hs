{-# LANGUAGE TypeApplications #-}

module Block4.TestTask3
  ( block4Task3TestGroup
  ) where

import LawsCheckers
import NonEmpty
import TestHelpers

import Hedgehog
import Test.Tasty
import Test.Tasty.Hedgehog

genNonempty :: Gen (NonEmpty Int)
genNonempty = do
  (x:xs) <- genIntList 1 1000
  pure $ x :| xs

genSmallNonempty :: Gen (NonEmpty Int)
genSmallNonempty = do
  (x:xs) <- genIntList 1 100
  pure $ x :| xs

block4Task3TestGroup :: TestTree
block4Task3TestGroup =
  testGroup
    "Block4.Task3"
    [ testProperty "Test NonEmpty functor id law" $ functorIdLaw genNonempty
    , testProperty "Test NonEmpty functor composition law" $ functorCompositionLaw genNonempty
    , testProperty "Test NonEmpty applicative identity law" $ applicativeIdentityLaw genNonempty
    , testProperty "Test NonEmpty applicative composition law" $
      applicativeCompositionLaw genSmallNonempty
    , testProperty "Test NonEmpty applicative homomorphism law" $
      applicativeHomomorphismLaw @NonEmpty
    , testProperty "Test NonEmpty applicative interchange law" $
      applicativeInterchangeLaw genSmallNonempty
    , testProperty "Test NonEmpty foldable foldMap" $ foldableFoldMap genNonempty
    , testProperty "Test NonEmpty foldable foldr" $ foldableFoldr genNonempty
    ]
