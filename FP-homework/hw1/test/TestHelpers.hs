{-# LANGUAGE TypeApplications #-}

module TestHelpers where

import Control.Exception
import Hedgehog
import qualified Hedgehog.Gen as Gen
import qualified Hedgehog.Range as Range
import Test.HUnit

assertErrorCall :: a -> Assertion
assertErrorCall f = do
  errored <- try @ErrorCall $ evaluate f
  case errored of
    (Right _) -> assertFailure "Error expected"
    (Left _)  -> pure ()

genIntList :: Int -> Int -> Gen [Int]
genIntList minLen maxLen =
  let listLength = Range.linear minLen maxLen
   in Gen.list listLength (Gen.enum (-50) 50)

genString :: Int -> Int -> Gen String
genString minLen maxLen =
  let listLength = Range.linear minLen maxLen
   in Gen.list listLength Gen.alpha
