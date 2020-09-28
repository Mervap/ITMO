module Task1.Internal
  ( -- * Some basic operations with points in Cartesian coordinates
    crossProductInternal
  , distInternal
  ) where

-- | Cross product of two dots: (x1, y1) and (x2, y2)
crossProductInternal :: Int -> Int -> Int -> Int -> Int
crossProductInternal x1 y1 x2 y2 = x1 * y2 - x2 * y1

-- | Distance between two dots: (x1, y1) and (x2, y2)
distInternal :: Int -> Int -> Int -> Int -> Double
distInternal x1 y1 x2 y2 = sqrt $ fromIntegral $ dx * dx + dy * dy
  where
    dx = x2 - x1
    dy = y2 - y1
