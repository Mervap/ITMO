module Block4.TestTask1
  ( block4Task1TestGroup
  ) where

import Block4.Task1

import Test.Tasty
import Test.Tasty.HUnit

testStringSum :: Assertion
testStringSum = do
  stringSum "1 2 3 4 5"         @?= Just 15
  stringSum "1       3"         @?= Just 4
  stringSum "-10    -20     42" @?= Just 12
  stringSum "1 f 2"             @?= Nothing
  stringSum "1 2 1f2 5"         @?= Nothing
  stringSum "1 + 2"             @?= Nothing
  stringSum "    "              @?= Just 0
  stringSum ""                  @?= Just 0

block4Task1TestGroup :: TestTree
block4Task1TestGroup = testGroup "Block4.Task1" [testCase "Test stringSum" testStringSum]
