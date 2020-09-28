{-# LANGUAGE BangPatterns #-}

module Task1.StrictPoint
  ( -- * Strict version of points in Cartesian coordinates with unpacked @Int@
    Point(..)

    -- * Operations with @Point@
  , plus
  , minus
  , scalarProduct
  , crossProduct
  , dist
  , perimeter
  , doubleArea
  ) where

import Control.DeepSeq (NFData(..), ($!!))

import Task1.Internal

-- | Strict Cartesian point representation
data Point = Point
  {-# UNPACK #-} !Int  -- ^ x coordinate
  {-# UNPACK #-} !Int  -- ^ y coordinate
  deriving (Show, Eq)

instance NFData Point where
  rnf (Point x y) = rnf x `seq` rnf y

-- | Sum of two 'Point's
plus :: Point -> Point -> Point
(Point x1 y1) `plus` (Point x2 y2) = Point (x1 + x2) (y1 + y2)

-- | Subtract of two 'Point's
minus :: Point -> Point -> Point
(Point x1 y1) `minus` (Point x2 y2) = Point (x1 - x2) (y1 - y2)

-- | Scalar product of two 'Point's
scalarProduct :: Point -> Point -> Int
(Point x1 y1) `scalarProduct` (Point x2 y2) = x1 * x2 + y1 * y2

-- | Cross product of two 'Point's
crossProduct :: Point -> Point -> Int
(Point x1 y1) `crossProduct` (Point x2 y2) = crossProductInternal x1 y1 x2 y2

-- | Distance between two 'Point's
dist :: Point -> Point -> Double
dist (Point x1 y1) (Point x2 y2) = distInternal x1 y1 x2 y2

pointPairTraverse ::
     (NFData a) => (a -> a -> a) -> (Point -> Point -> a) -> a -> [Point] -> a
pointPairTraverse _ _ defVal [] = defVal
pointPairTraverse collapse fun defVal list@(hl:_) = accCalc defVal hl list
  where
    accCalc acc h [x] = (collapse $!! acc) $!! fun x h
    accCalc acc h (x:g@(y:_)) =
      let !newAcc = (collapse $!! acc) $!! fun x y
       in accCalc newAcc h g
    accCalc _ _ _ = undefined -- never

-- | Perimeter of polygon without self-intersections, defined by the list of
-- 'Point's in counterclockwise order
perimeter :: [Point] -> Double
perimeter = pointPairTraverse (+) dist 0

-- | Double area of polygon without self-intersections, defined by the list of
-- 'Point's in counterclockwise order
doubleArea :: [Point] -> Int
doubleArea = abs . pointPairTraverse (+) crossProduct 0
