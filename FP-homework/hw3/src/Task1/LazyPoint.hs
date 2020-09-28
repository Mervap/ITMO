module Task1.LazyPoint
  ( -- * Simple lazy version of points in Cartesian coordinates
    PointLazy(..)

    -- * Operations with @PointLazy@
  , crossProductLazy
  , distLazy
  , perimeterLazy
  , doubleAreaLazy
  ) where

import Control.DeepSeq (NFData(..))

import Task1.Internal

-- | Lazy Cartesian point representation
data PointLazy = PointLazy
  Int -- ^ x coordinate 
  Int -- ^ y coordinate
  deriving (Show, Eq)

-- | Cross product of two 'PointLazy'
crossProductLazy :: PointLazy -> PointLazy -> Int
(PointLazy x1 y1) `crossProductLazy` (PointLazy x2 y2) =
  crossProductInternal x1 y1 x2 y2

-- | Distance between two 'PointLazy'
distLazy :: PointLazy -> PointLazy -> Double
distLazy (PointLazy x1 y1) (PointLazy x2 y2) = distInternal x1 y1 x2 y2

pointPairTraverseLazy ::
     (NFData a)
  => (a -> a -> a)
  -> (PointLazy -> PointLazy -> a)
  -> a
  -> [PointLazy]
  -> a
pointPairTraverseLazy _ _ defVal [] = defVal
pointPairTraverseLazy collapse fun defVal list@(hl:_) = accCalc defVal hl list
  where
    accCalc acc h [x] = collapse acc $ fun x h
    accCalc acc h (x:g@(y:_)) = accCalc (collapse acc $ fun x y) h g
    accCalc _ _ _ = undefined  -- never happens

-- | Perimeter of polygon without self-intersections, defined by the list of 
-- 'PointLazy' in counterclockwise order
perimeterLazy :: [PointLazy] -> Double
perimeterLazy = pointPairTraverseLazy (+) distLazy 0

-- | Double area of polygon without self-intersections, defined by the list of 
-- 'PointLazy' in counterclockwise order
doubleAreaLazy :: [PointLazy] -> Int
doubleAreaLazy = abs . pointPairTraverseLazy (+) crossProductLazy 0
