{-# LANGUAGE BangPatterns #-}

module BenchTask2
  ( benchTask2
  ) where

import BenchInternal (benchWithReport)
import Control.Concurrent (getNumCapabilities)
import Control.Concurrent.Async (forConcurrently)
import Control.Monad (replicateM)
import Criterion.Main
import qualified StmContainers.Map as SM
import System.Random

import Task2.ConcurrentHashTable
import Task2.MapRequest

-- | Generates a random sequence of 'Request's with the probability of a
-- 'Lookup' query @lookProb@
generateRequestList :: Int -> Float -> (Int, Int) -> IO [Request Int Int]
generateRequestList cnt lookProb keyRange
  | cnt <= 0 = return []
  | otherwise = do
    p <- randomIO :: IO Float
    k <- randomRIO keyRange
    a <- randomIO
    let op =
          if p < lookProb
            then Lookup k
            else Insert k a
    (op :) <$> generateRequestList (cnt - 1) lookProb keyRange

benchTask2 :: IO ()
benchTask2 = do
  singleThGroup <- benchThreadGroup 1
  maxThreads    <- getNumCapabilities
  halfThGroup   <- benchThreadGroup (maxThreads `div` 2)
  maxThGroup    <- benchThreadGroup maxThreads
  benchWithReport "Task2" [singleThGroup, halfThGroup, maxThGroup]

-- | Runs tests with the number of threads @numThreads@. 
-- Comparison is made with 'StmContainers.Map'
benchThreadGroup :: Int -> IO Benchmark
benchThreadGroup numThreads = do
  let numRequests = 10 ^ (6 :: Int)
  let keyRange    = (1, 10 ^ (3 :: Int))
  let genTests    = generateRequestList (numRequests `div` numThreads)
  let mkCHT       = newCHT   :: IO (ConcurrentHashTable Int Int)
  let mkStmMap    = SM.newIO :: IO (SM.Map Int Int)
  let eval tests ds = forConcurrently tests (mapM $ \r -> runRequest r ds)
  !tests_80_20 <- replicateM numThreads $ genTests 0.8 keyRange
  !tests_50_50 <- replicateM numThreads $ genTests 0.5 keyRange
  !tests_20_80 <- replicateM numThreads $ genTests 0.2 keyRange
  return $
    bgroup
      (show numThreads ++ "-threads")
      [ bgroup
          "80% Lookups; 20% Inserts"
          [ bench "STM Map"  $ nfIO (mkStmMap >>= eval tests_80_20)
          , bench "CHT (my)" $ nfIO (mkCHT    >>= eval tests_80_20)
          ]
      , bgroup
          "50% Lookups; 50% Inserts"
          [ bench "STM Map"  $ nfIO (mkStmMap >>= eval tests_50_50)
          , bench "CHT (my)" $ nfIO (mkCHT    >>= eval tests_50_50)
          ]
      , bgroup
          "20% Lookups; 80% Inserts"
          [ bench "STM Map"  $ nfIO (mkStmMap >>= eval tests_20_80)
          , bench "CHT (my)" $ nfIO (mkCHT    >>= eval tests_20_80)
          ]
      ]
