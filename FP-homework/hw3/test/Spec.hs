import Test.Tasty

import Task1
import Task2
import Task3
import Task4
import Task5
import Task6
import Task7

main :: IO ()
main = do
  task2TestGroup <- getTask2TestGroup
  defaultMain $
    testGroup "Tests" 
    [ task1TestGroup
    , task2TestGroup
    , task3TestGroup
    , task4TestGroup
    , task5TestGroup
    , task6TestGroup
    , task7TestGroup
    ]
