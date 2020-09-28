module BenchTask1
  ( benchTask1
  ) where

import Task1.LazyPoint
import Task1.StrictPoint

import BenchInternal
import Control.DeepSeq (deepseq)
import Criterion.Main (bench, bgroup, nf)

getData :: (Int -> Int -> b) -> [b]
getData constructor = zipWith constructor [1..10 ^ (7 :: Int)] [1..10 ^ (7 :: Int)]

pointData :: [Point]
pointData = getData Point

lazyPointData :: [PointLazy]
lazyPointData = getData PointLazy

benchTask1 :: IO ()
benchTask1 =
  benchWithReport
    "Task1"
    [ bgroup
        "Perimeter"
        [ deepseq pointData $ bench "Strict" $ nf perimeter pointData
        , seq lazyPointData $ bench "Lazy"   $ nf perimeterLazy lazyPointData
        ]
    , bgroup
        "Area"
        [ deepseq pointData $ bench "Strict" $ nf doubleArea pointData
        , seq lazyPointData $ bench "Lazy"   $ nf doubleAreaLazy lazyPointData
        ]
    ]
