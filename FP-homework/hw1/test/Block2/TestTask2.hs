module Block2.TestTask2
  ( block2Task2TestGroup
  ) where

import Block2.Task2
import TestHelpers

import Data.List.NonEmpty (fromList)
import Hedgehog
import qualified Hedgehog.Gen as Gen
import Test.Tasty
import Test.Tasty.Hedgehog
import Test.Tasty.HUnit

propSplitJoin :: Property
propSplitJoin =
  property $ do
    string <- forAll $ genString 0 1000
    delim <- forAll Gen.alpha
    joinWith delim (splitOn delim string) === string

testSplitOn :: Assertion
testSplitOn = do
  splitOn '/' "path/to/file" @?= fromList ["path", "to", "file"]
  splitOn '/' "path/to/file/" @?= fromList ["path", "to", "file", ""]
  splitOn '/' "/path/to/file" @?= fromList ["", "path", "to", "file"]
  splitOn '/' "path//to/file" @?= fromList ["path", "", "to", "file"]

testJoinWith :: Assertion
testJoinWith = do
  joinWith '/' (fromList ["path", "to", "file"]) @?= "path/to/file"
  joinWith '/' (fromList ["path", "to", "file", ""]) @?= "path/to/file/"
  joinWith '/' (fromList ["", "path", "to", "file"]) @?= "/path/to/file"
  joinWith '/' (fromList ["path", "", "to", "file"]) @?= "path//to/file"

block2Task2TestGroup :: TestTree
block2Task2TestGroup =
  testGroup
    "Block2.Task2"
    [ testCase "Test splitOn" testSplitOn
    , testCase "Test joinWith" testJoinWith
    , testProperty "Test splitJoinWith property" propSplitJoin
    ]
