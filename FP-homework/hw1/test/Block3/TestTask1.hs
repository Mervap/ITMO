module Block3.TestTask1
  ( block3Task1TestGroup
  ) where

import Block3.Task1

import Test.Tasty
import Test.Tasty.HUnit

testMaybeConcat :: Assertion
testMaybeConcat = do
  maybeConcat [Just [1 :: Int, 2, 3], Nothing, Just [4, 5]] @?= [1, 2, 3, 4, 5]
  maybeConcat [Nothing :: Maybe [Int], Nothing] @?= []
  maybeConcat [Nothing :: Maybe [Int], Just [], Nothing] @?= []
  maybeConcat ([] :: [Maybe [Int]]) @?= []

block3Task1TestGroup :: TestTree
block3Task1TestGroup = testGroup "Block3.Task1" [testCase "Test maybeConcat" testMaybeConcat]
