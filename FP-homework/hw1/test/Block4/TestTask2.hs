{-# LANGUAGE TypeApplications #-}

module Block4.TestTask2
  ( block4Task2TestGroup
  ) where

import Block4.Task2
import LawsCheckers

import Hedgehog
import qualified Hedgehog.Gen as Gen
import Test.Tasty
import Test.Tasty.Hedgehog

genTree :: Int -> Gen (Tree Int)
genTree h = do
  val <- Gen.enum (-100 :: Int) 100
  if h > 9
    then return $ Leaf val
    else do
      left <- genTree (h + 1)
      right <- genTree (h + 1)
      Gen.element [Leaf val, Branch left right]

genTreeInitial :: Gen (Tree Int)
genTreeInitial = genTree 0

block4Task2TestGroup :: TestTree
block4Task2TestGroup =
  testGroup
    "Block4.Task2"
    [ testProperty "Test tree functor id law" $ functorIdLaw genTreeInitial
    , testProperty "Test tree functor composition law" $ functorCompositionLaw genTreeInitial
    , testProperty "Test tree applicative identity law" $ applicativeIdentityLaw genTreeInitial
    , testProperty "Test tree applicative composition law" $
      applicativeCompositionLaw genTreeInitial
    , testProperty "Test tree applicative homomorphism law" $ applicativeHomomorphismLaw @Tree
    , testProperty "Test tree applicative interchange law" $
      applicativeInterchangeLaw genTreeInitial
    , testProperty "Test tree foldable foldMap" $ foldableFoldMap genTreeInitial
    , testProperty "Test tree foldable foldr" $ foldableFoldr genTreeInitial
    ]
