{-# LANGUAGE ScopedTypeVariables #-}

module Task7
  (
    -- * Exercises on typification of expressions.
    subtask1Def
  , subtask2Def
  , subtask3Def
  , subtask1
  , subtask2
  , subtask3
  ) where

import Data.Either

-- | Subtask 1 exactly.
subtask1Def :: Bool
subtask1Def = null . head $ map (uncurry id) [((++) "Dorian ", " Grey")]

-- | Typification of all subterm of 'subtask1Def'.
subtask1 :: Bool
subtask1 = ((nullHead `dollar`) :: ([String] -> Bool)) dorianGrey
  where
    nullHead :: [String] -> Bool
    nullHead = dottedNull headF
      where
        dottedNull :: ([String] -> String) -> [String] -> Bool
        dottedNull = (nullF `dot`)
          where
            nullF :: String -> Bool
            nullF = null
            dot :: (String -> Bool) -> ([String] -> String) -> [String] -> Bool
            dot = (.)
        headF :: [String] -> String
        headF = head
    dollar :: ([String] -> Bool) -> [String] -> Bool
    dollar = ($)
    dorianGrey :: [String]
    dorianGrey = merger dorianGreyList
      where
        merger :: [(String -> String, String)] -> [String]
        merger = mapF (uncurryF idF :: (String -> String, String) -> String)
          where
            mapF
              :: ((String -> String, String) -> String)
              -> [(String -> String, String)]
              -> [String]
            mapF = map
            uncurryF
              :: ((String -> String) -> String -> String)
              -> (String -> String, String)
              -> String
            uncurryF = uncurry
            idF :: (String -> String) -> (String -> String)
            idF = id
        dorianGreyList :: [(String -> String, String)]
        dorianGreyList = connPair ([] :: [(String -> String, String)])
          where
            connPair :: [(String -> String, String)] -> [(String -> String, String)]
            connPair = (pair :)
            pair :: (String -> String, String)
            pair = (comma left :: String -> (String -> String, String)) right
              where
                comma :: (String -> String) -> String -> (String -> String, String)
                comma = (,)
                left :: (String -> String)
                left = ((++) :: String -> String -> String) ("Dorian " :: String)
                right :: String
                right = " Grey"

-- | Subtask 2 exactly.
subtask2Def :: [(Integer, Integer)]
subtask2Def = (\x -> zip (lefts x) (rights x)) [Left (1 + 2), Right (2 ^ 6)]

-- | Typification of all subterm of 'subtask2Def'.
subtask2 :: [(Integer, Integer)]
subtask2 = unwrapper list
  where
    unwrapper :: [Either Integer Integer] -> [(Integer, Integer)]
    unwrapper (x :: [Either Integer Integer]) = zipF unwrappedRights
      where
        zipF :: [Integer] -> [(Integer, Integer)]
        zipF = (zip :: [Integer] -> [Integer] -> [(Integer, Integer)]) unwrappedLefts
        unwrappedLefts :: [Integer]
        unwrappedLefts = lefts (x :: [Either Integer Integer])
        unwrappedRights :: [Integer]
        unwrappedRights = rights (x :: [Either Integer Integer])
    list :: [Either Integer Integer]
    list = fstElem : (sndElem : emptyList :: [Either Integer Integer])
      where
        fstElem :: Either Integer Integer
        fstElem = leftConstructor onePlusTwo
          where
            leftConstructor :: Integer -> Either Integer Integer
            leftConstructor = Left
            onePlusTwo :: Integer
            onePlusTwo = (plus (1 :: Integer) :: Integer -> Integer) (2 :: Integer)
              where
                plus :: Integer -> Integer -> Integer
                plus = (+)
        sndElem :: Either Integer Integer
        sndElem = rightConstructor twoPowerSix
          where
            rightConstructor :: Integer -> Either Integer Integer
            rightConstructor = Right
            twoPowerSix :: Integer
            twoPowerSix = (power (2 :: Integer) :: Integer -> Integer) (6 :: Integer)
              where
                power :: Integer -> Integer -> Integer
                power = (^)
        emptyList :: [Either Integer Integer]
        emptyList = []

-- | Subtask 3 exactly.
subtask3Def :: Integer -> Bool
subtask3Def =
  let impl = \x y -> not x || y
   in let isMod2 = \x -> x `mod` 2 == 0
       in let isMod4 = \x -> x `mod` 4 == 0
           in \x -> (isMod4 x) `impl` (isMod2 x)

-- | Typification of all subterm of 'subtask3Def'.
subtask3 :: Integer -> Bool
subtask3 =
  let (impl :: Bool -> Bool -> Bool) = implF
   in let (isMod2 :: Integer -> Bool) = isMod2F
       in let (isMod4 :: Integer -> Bool) = isMod4F
           in \(x :: Integer) ->
                ((isMod4 (x :: Integer) `impl`) :: Bool -> Bool)
                (isMod2 (x :: Integer))
  where
    implF :: Bool -> Bool -> Bool
    implF (x :: Bool) (y :: Bool) = (orF notX :: Bool -> Bool) (y :: Bool)
      where
        notX :: Bool
        notX = (not :: Bool -> Bool) (x :: Bool)
        orF :: Bool -> Bool -> Bool
        orF = (||)
    isMod2F :: Integer -> Bool
    isMod2F (x :: Integer) = (eq xModTwo :: Integer -> Bool) (0 :: Integer)
      where
        xModTwo :: Integer
        xModTwo = xMod (2 :: Integer)
          where
            xMod :: Integer -> Integer
            xMod = (mod :: Integer -> Integer -> Integer) (x :: Integer)
        eq :: Integer -> Integer -> Bool
        eq = (==)
    isMod4F :: Integer -> Bool
    isMod4F (x :: Integer) = (eq xModFour :: Integer -> Bool) (0 :: Integer)
      where
        xModFour :: Integer
        xModFour = xMod (4 :: Integer)
          where
            xMod :: Integer -> Integer
            xMod = (mod :: Integer -> Integer -> Integer) (x :: Integer)
        eq :: Integer -> Integer -> Bool
        eq = (==)
