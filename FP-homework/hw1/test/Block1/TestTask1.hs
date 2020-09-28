module Block1.TestTask1
  ( block1Task1TestGroup
  ) where

import Block1.Task1

import Test.Tasty
import Test.Tasty.HUnit

testForEachOfDay :: (Show a, Eq a) => [a] -> (DayOfTheWeek -> a) -> Assertion
testForEachOfDay expected fun =
  assertEqual
    ""
    expected
    (map fun [Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday])

testNextDay :: Assertion
testNextDay =
  testForEachOfDay
    [Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, Monday]
    nextDay

testAfterDays :: Assertion
testAfterDays = do 
  afterDays 3 Tuesday       @?= Friday
  afterDays (10 ^ 9) Monday @?= Sunday

testIsWeekend :: Assertion
testIsWeekend =
  testForEachOfDay 
    [False, False, False, False, False, True, True] 
    isWeekend

testDaysToParty :: Assertion
testDaysToParty = 
  testForEachOfDay
    [4, 3, 2, 1, 0, 6, 5] 
    daysToParty

block1Task1TestGroup :: TestTree
block1Task1TestGroup =
  testGroup
    "Block1.Task1"
    [ testCase "Test nextDay" testNextDay
    , testCase "Test afterDays" testAfterDays
    , testCase "Test isWeekend" testIsWeekend
    , testCase "test daysToParty" testDaysToParty
    ]
