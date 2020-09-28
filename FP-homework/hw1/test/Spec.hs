import Block1.TestTask1
import Block1.TestTask2
import Block1.TestTask3
import Block2.TestTask1
import Block2.TestTask2
import Block3.TestTask1
import Block3.TestTask2
import Block4.TestTask1
import Block4.TestTask2
import Block4.TestTask3
import Block5.TestTask1
import Block5.TestTask2

import Test.Tasty

main :: IO ()
main =
  defaultMain $
  testGroup
    "Tests"
    [ block1Task1TestGroup
    , block1Task2TestGroup
    , block1Task3TestGroup
    , block2Task1TestGroup
    , block2Task2TestGroup
    , block3Task1TestGroup
    , block3Task2TestGroup
    , block4Task1TestGroup
    , block4Task2TestGroup
    , block4Task3TestGroup
    , block5Task1TestGroup
    , block5Task2TestGroup
    ]
