module Main where

import Data.Char

-------------------------
-- Practice 1
-------------------------
------------------------------------------
-- Task 1
--
-- Implement task 2 from block 3 (castles)
-- of homework located at
-- https://hackmd.io/@mtb49Ag9TOmTeG0qf_6Fwg/BkcpMch44
--
------------------------------------------

data ResidentialBuilding
  = One
  | Two
  | Three
  | Four
  deriving (Show)

data ResidentialBuildingList =
  ResidentialBuildingList ResidentialBuilding [ResidentialBuilding]
  deriving (Show)

data PublicBuilding
  = Library
  | Church
  | NoPB
  deriving (Show)

data Castle
  = NoCastle
  | Castle
      { hasALord :: Bool
      , hasAWall :: Bool
      }
  deriving (Show)

data City =
  City Castle PublicBuilding ResidentialBuildingList
  deriving (Show)

buildCastle :: City -> City
buildCastle (City NoCastle pb rb) = City (Castle False False) pb rb
buildCastle city                  = city

buildPublicBuilding :: PublicBuilding -> City -> City
buildPublicBuilding pb (City castle NoPB rb) = City castle pb rb
buildPublicBuilding _ city                   = city

buildResidentialBuilding :: ResidentialBuilding -> City -> City
buildResidentialBuilding rb (City castle pb (ResidentialBuildingList x xs)) =
  City castle pb (ResidentialBuildingList x (rb : xs))

hiLord :: City -> Either City String
hiLord (City NoCastle _ _) = Right "No castle in city"
hiLord (City castle pb rb)
  | hasALord castle = Right "Lord already exist"
  | otherwise = Left $ City castle {hasALord = True} pb rb

buildWalls :: City -> City
buildWalls city@(City castle pb rb)
  | hasALord castle && tenh rb = City castle {hasAWall = True} pb rb
  | otherwise = city
  where
    tenh :: ResidentialBuildingList -> Bool
    tenh (ResidentialBuildingList x xs) = foldl counter 0 (x : xs) >= 10
    counter :: Int -> ResidentialBuilding -> Int
    counter i One   = i + 1
    counter i Two   = i + 2
    counter i Three = i + 3
    counter i Four  = i + 4

-----------------------------------------
-- Task 2
--
-- Implement `Ord` instance for newtype
-- below.
--
-- It should follow the described semantics:
--
--  1) If both strings being compared start
--  from a digit symbol (`0..9`), read
--  numeric prefixes and compare strings by
--  these prefixes. If both strings start
--  from the same number (e.g. `01aba` and
--  `1caba`), comparison is performed by
--  rest of string characters
--  case-insensitive
--  2) Otherwise, compare two strings
--  case-insensitive
--------------------------------------------
newtype FName =
  FName String

getNumPrefix :: String -> String
getNumPrefix [] = []
getNumPrefix (x:xs)
  | isDigit x = x : getNumPrefix xs
  | otherwise = []

instance Eq FName where
  a == b = compare a b == EQ

instance Ord FName where
  compare (FName s1) (FName s2)
    | s1Prefix <- getNumPrefix s1
    , s2Prefix <- getNumPrefix s2
    , not $ null s1Prefix || null s2Prefix =
      case compare (read s1Prefix :: Int) (read s2Prefix :: Int) of
        EQ -> compare (drop (length s1Prefix) s1) (drop (length s2Prefix) s2)
        res -> res
    | otherwise = compare s1 s2

--------------------------------------------
-- Task 3
--
-- When launched from ghci, following
-- results will be printed.
-- Explain the difference in calls
-- (why one call returns while other
-- goes into infinite loop):
--
-- > sumAndLogD [8] (BoxD 2)
-- Just 3.0
--
-- > sumAndLog [8] (Box 2)
-- Just 3.0
--
-- > sumAndLog [8 -10] loop
-- Nothing
--
-- > sumAndLogD [8 -10] loop
-- {.. infitinte loop ..}
--
--------------------------------------------
newtype Box a =
  Box a

data BoxD a =
  BoxD a

sumAndLog as (Box base) =
  let s = sum as
   in if s < 0
        then Nothing
        else Just (log s / log base)

sumAndLogD as (BoxD _) =
  let s = sum as
   in if s < 0
        then Nothing
        else Just (log s )

loop = loop

main = pure ()
