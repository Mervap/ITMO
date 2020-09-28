module Block5.TestTask1
  ( block5Task1TestGroup
  ) where

import Block5.Task1

import Test.Tasty
import Test.Tasty.HUnit

testCorrectExpr :: Assertion
testCorrectExpr = do
  evalExpr (Add (Const 2) (Const 2)) @?= Right 4
  evalExpr (Pow (Add (Const 2) (Const 3)) (Const 2)) @?= Right 25
  evalExpr (Pow (Sub (Const 2) (Const 4)) (Const 3)) @?= Right (-8)
  evalExpr (Div (Mul (Const 2) (Const 4)) (Const 3)) @?= Right 2

testDivisionByZero :: Assertion
testDivisionByZero = do
  evalExpr (Div (Const 5) (Const 0)) @?= Left DivisionByZero
  evalExpr (Div (Add (Const 5) (Const 6)) (Sub (Const 5) (Const 5))) @?= Left DivisionByZero
  evalExpr (Div (Const 0) (Mul (Const 5) (Const 0))) @?= Left DivisionByZero

testNegatePow :: Assertion
testNegatePow = do
  evalExpr (Pow (Const 5) (Const (-10))) @?= Left NegativePower
  evalExpr (Pow (Const 11) (Sub (Const 5) (Const 10))) @?= Left NegativePower
  evalExpr (Pow (Const (-1)) (Mul (Const 5) (Const (-1)))) @?= Left NegativePower

block5Task1TestGroup :: TestTree
block5Task1TestGroup =
  testGroup
    "Block5.Task1"
    [ testCase "Test correct expr" testCorrectExpr
    , testCase "Test division by zero" testDivisionByZero
    , testCase "Test negate pow" testNegatePow
    ]
