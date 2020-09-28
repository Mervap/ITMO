module Block1.TestTask2
  ( block1Task2TestGroup
  ) where

import Block1.Task2
import TestHelpers

import Test.Tasty
import Test.Tasty.HUnit

testToInteger :: Assertion
testToInteger = do
  toIntegerNat Z @?= 0
  toIntegerNat (S $ S Z) @?= 2
  toIntegerNat (S $ S $ S $ S Z) @?= 4

testFromInteger :: Assertion
testFromInteger = do
  fromInteger 0 @?= Z
  fromInteger 2 @?= (S $ S Z)
  fromInteger 4 @?= (S $ S $ S $ S Z)

testAdd :: Assertion
testAdd = do
  (1 :: Nat) + (3 :: Nat) @?= (4 :: Nat)
  (2 :: Nat) + (2 :: Nat) @?= (4 :: Nat)
  (20 :: Nat) + (22 :: Nat) @?= (42 :: Nat)

testMul :: Assertion
testMul = do
  (0 :: Nat) * (10 :: Nat) @?= (0 :: Nat)
  (10 :: Nat) * (0 :: Nat) @?= (0 :: Nat)
  (1 :: Nat) * (3 :: Nat) @?= (3 :: Nat)
  (2 :: Nat) * (2 :: Nat) @?= (4 :: Nat)
  (10 :: Nat) * (22 :: Nat) @?= (220 :: Nat)

testSub :: Assertion
testSub = do
  (3 :: Nat) - (1 :: Nat) @?= (2 :: Nat)
  (1 :: Nat) - (3 :: Nat) @?= (0 :: Nat)
  (2 :: Nat) - (2 :: Nat) @?= (0 :: Nat)
  (22 :: Nat) - (12 :: Nat) @?= (10 :: Nat)

testEq :: Assertion
testEq = do
  (42 :: Nat) == (42 :: Nat) @?= True
  (0 :: Nat) == (42 :: Nat) @?= False
  (42 :: Nat) == (0 :: Nat) @?= False

testLe :: Assertion
testLe = do
  (42 :: Nat) < (42 :: Nat) @?= False
  (15 :: Nat) < (42 :: Nat) @?= True
  (42 :: Nat) < (10 :: Nat) @?= False

testGe :: Assertion
testGe = do
  (42 :: Nat) > (42 :: Nat) @?= False
  (15 :: Nat) > (42 :: Nat) @?= False
  (42 :: Nat) > (10 :: Nat) @?= True

testCompareGroup :: TestTree
testCompareGroup =
  testGroup
    "Test compare"
    [testCase "Test EQ" testEq, testCase "Test LT" testLe, testCase "Test GT" testGe]

testEven :: Assertion
testEven = do
  evenNat (10 :: Nat) @?= True
  evenNat (0 :: Nat) @?= True
  evenNat (1 :: Nat) @?= False
  evenNat (15 :: Nat) @?= False

testDiv :: Assertion
testDiv = do
  (14 :: Nat) `divNat` (3 :: Nat) @?= (4 :: Nat)
  (35 :: Nat) `divNat` (6 :: Nat) @?= (5 :: Nat)
  (0 :: Nat) `divNat` (12 :: Nat) @?= (0 :: Nat)
  assertErrorCall (42 `divNat` 0)

testMod :: Assertion
testMod = do
  (14 :: Nat) `modNat` (3 :: Nat) @?= (2 :: Nat)
  (35 :: Nat) `modNat` (6 :: Nat) @?= (5 :: Nat)
  (0 :: Nat) `modNat` (12 :: Nat) @?= (0 :: Nat)
  (10 :: Nat) `modNat` (12 :: Nat) @?= (10 :: Nat)
  assertErrorCall (42 `divNat` 0)

block1Task2TestGroup :: TestTree
block1Task2TestGroup =
  testGroup
    "Block1.Task2"
    [ testCase "Test toInteger" testToInteger
    , testCase "Test fromInteger" testFromInteger
    , testCase "Test add" testAdd
    , testCase "Test mul" testMul
    , testCase "Test sub" testSub
    , testCompareGroup
    , testCase "Test even" testEven
    , testCase "Test div" testDiv
    , testCase "Test mod" testMod
    ]
