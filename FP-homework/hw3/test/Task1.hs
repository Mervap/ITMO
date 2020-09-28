module Task1
  ( task1TestGroup
  ) where

import Control.Monad (unless)
import Data.AEq (AEq, (~==))
import Test.Tasty
import Test.Tasty.HUnit

import Task1.StrictPoint

infix 1 @?~=

-- | Asserts that the specified actual value is approximate equal to the expected
-- value (with the actual value on the left-hand side).
(@?~=) ::
     (AEq a, Show a, HasCallStack)
  => a -- ^ The actual value
  -> a -- ^ The expected value
  -> Assertion
actual @?~= expected = unless (actual ~== expected) (assertFailure msg)
  where
    msg = "expected: " ++ show expected ++ "\n but got: " ++ show actual

testPerimeter :: Assertion
testPerimeter = do
  perimeter [Point 1 1, Point 0 1, Point 0 0, Point 1 0] @?~= 4.0
  perimeter [Point 1 2, Point 1 1, Point 2 1] @?~= 2.0 + sqrt 2.0
  perimeter
    [ Point 0 0
    , Point 4 0
    , Point 4 (-2)
    , Point 10 (-2)
    , Point 10 6
    , Point 8 6
    , Point 8 10
    , Point 0 10
    ] @?~=
    44.0

testArea :: Assertion
testArea = do
  doubleArea [Point 1 1, Point 0 1, Point 0 0, Point 1 0] @?= 2
  doubleArea [Point 1 2, Point 1 1, Point 2 1] @?= 1
  doubleArea
    [ Point 0 0
    , Point 4 0
    , Point 4 (-2)
    , Point 10 (-2)
    , Point 10 6
    , Point 8 6
    , Point 8 10
    , Point 0 10
    ] @?=
    208

task1TestGroup :: TestTree
task1TestGroup =
  testGroup
    "Task1"
    [testCase "Test perimeter" testPerimeter, testCase "Test area" testArea]
