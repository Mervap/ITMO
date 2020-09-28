module Main where

import BenchTask1
import BenchTask2

-- | Launches the benchmark for task 1 and 2. Automatically generates a criterion 
-- report for them in the @report@ directory. To avoid ambiguities the repository also 
-- contains the results of benchmarks on my local machine in the @localReport@ directory
main :: IO ()
main = do 
  benchTask1
  benchTask2
