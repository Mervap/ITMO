module Block3.TestTask2
  ( block3Task2TestGroup
  ) where

import Block3.Task2
import TestHelpers

import Hedgehog
import qualified Hedgehog.Gen as Gen
import Test.Tasty
import Test.Tasty.Hedgehog
import Test.Tasty.HUnit

propNonEmptySemigroup :: Property
propNonEmptySemigroup =
  property $ do
    x <- forAll $ genIntList 1 1000
    y <- forAll $ genIntList 1 1000
    z <- forAll $ genIntList 1 1000
    let [nx, ny, nz] = map fromList [x, y, z]
    nx <> (ny <> nz) === (nx <> ny) <> nz

genThisOrThat :: Gen (ThisOrThat (Maybe String) (Maybe [Int]))
genThisOrThat = do
  thisVal <- Gen.maybe $ genString 0 1000
  thatVal <- Gen.maybe $ genIntList 0 1000
  Gen.element [This thisVal, That thatVal, Both thisVal thatVal]

propThisOrTharSemigroup :: Property
propThisOrTharSemigroup =
  property $ do
    x <- forAll genThisOrThat
    y <- forAll genThisOrThat
    z <- forAll genThisOrThat
    x <> (y <> z) === (x <> y) <> z

testNonEmptyConcat :: Assertion
testNonEmptyConcat = do
  (10 :: Int) :| [15] <> 5 :| [42] @?= 10 :| [15, 5, 42]
  (10 :: Int) :| [] <> 5 :| [] @?= 10 :| [5]

testThisOrThatConcat :: Assertion
testThisOrThatConcat = do
  let getThis s = This s :: ThisOrThat String String
  let getThat s = That s :: ThisOrThat String String
  let getBoth a b = Both a b :: ThisOrThat String String
  getThis "a" <> getThis "b" @?= getThis "ab"
  getThat "b" <> getThat "a" @?= getThat "ba"
  getThat "c" <> getThis "d" @?= getBoth "d" "c"
  getBoth "a" "b" <> getThis "c" @?= getBoth "ac" "b"
  getBoth "a" "b" <> getThat "c" @?= getBoth "a" "bc"

block3Task2TestGroup :: TestTree
block3Task2TestGroup =
  testGroup
    "Block3.Task2"
    [ testCase "Test thisOrThatConcat" testThisOrThatConcat
    , testProperty "Test nonEmpty semigroup property" propNonEmptySemigroup
    , testProperty "Test thisOrThar semigroup property" propThisOrTharSemigroup
    ]
