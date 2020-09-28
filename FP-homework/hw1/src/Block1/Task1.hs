{-# LANGUAGE InstanceSigs #-}

module Block1.Task1
   (
    -- * The type of days of the week
    DayOfTheWeek(..)
    
    -- * Operations with days of the week
  , nextDay
  , afterDays
  , isWeekend
  , daysToParty
  ) where

data DayOfTheWeek
  = Monday
  | Tuesday
  | Wednesday
  | Thursday
  | Friday
  | Saturday
  | Sunday
  deriving (Show)

instance Enum DayOfTheWeek where
  
  toEnum :: Int -> DayOfTheWeek
  toEnum 0 = Monday
  toEnum 1 = Tuesday
  toEnum 2 = Wednesday
  toEnum 3 = Thursday
  toEnum 4 = Friday
  toEnum 5 = Saturday
  toEnum 6 = Sunday
  toEnum _ = error "In week only 7 days"
  
  fromEnum :: DayOfTheWeek -> Int
  fromEnum Monday    = 0
  fromEnum Tuesday   = 1
  fromEnum Wednesday = 2
  fromEnum Thursday  = 3
  fromEnum Friday    = 4
  fromEnum Saturday  = 5
  fromEnum Sunday    = 6

instance Eq DayOfTheWeek where
  day1 == day2 = fromEnum day1 == fromEnum day2

nextDay :: DayOfTheWeek -> DayOfTheWeek
nextDay = afterDays 1

afterDays :: Int -> DayOfTheWeek -> DayOfTheWeek
afterDays cnt = toEnum . ((`mod` 7) . (+ cnt)) . fromEnum

isWeekend :: DayOfTheWeek -> Bool
isWeekend = (> 4) . fromEnum

daysToParty :: DayOfTheWeek -> Int
daysToParty = (`mod` 7) . (+ 7) . (4 -) . fromEnum
