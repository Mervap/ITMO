{-# LANGUAGE TypeApplications #-}

module Block5.TestTask2
  ( block5Task2TestGroup
  ) where

import Block5.Task2
import TestHelpers

import Hedgehog
import Test.Tasty
import Test.Tasty.Hedgehog
import Test.Tasty.HUnit

testMoving :: Assertion
testMoving = do
  moving 4 [1 :: Int, 5, 3, 8, 7, 9, 6] @?= [1.0 :: Double, 3.0, 3.0, 4.25, 5.75, 6.75, 7.5]
  moving 2 [1 :: Int, 5, 3, 8, 7, 9, 6] @?= [1.0 :: Double, 3.0, 4.0, 5.5, 7.5, 8.0, 7.5]

testIdMoving :: Property
testIdMoving =
  property $ do
    list <- forAll $ genIntList 0 1000
    moving 1 list === map (realToFrac @Int @Double) list

block5Task2TestGroup :: TestTree
block5Task2TestGroup =
  testGroup
    "Block5.Task2"
    [testCase "Test moving" testMoving, testProperty "Test id moving property" testIdMoving]
