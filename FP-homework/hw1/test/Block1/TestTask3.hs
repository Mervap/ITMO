module Block1.TestTask3
  ( block1Task3TestGroup
  ) where

import Block1.Task3
import Tree

import Data.List.NonEmpty (NonEmpty (..), fromList)
import Test.Tasty
import Test.Tasty.HUnit

testEmpty :: Assertion
testEmpty = do
  isEmpty List @?= True
  isEmpty (Branch List ("a" :| []) List) @?= False

testSize :: Assertion
testSize = do
  size List @?= 0
  size (Branch List (fromList ["a", "a", "a"]) List) @?= 3
  size bigTree @?= 6
  where
    bigTree =
      Branch
        (Branch List (fromList ["a", "a", "a"]) List)
        (fromList ["d", "d"])
        (Branch List (fromList ["f"]) List)

findElementInTestTree :: Int -> Maybe Int
findElementInTestTree =
  find $
  Branch
    (Branch
       (Branch List (fromList [1, 1, 1]) List)
       (fromList [2])
       (Branch List (fromList [4, 4, 4, 4]) List))
    (fromList [7, 7])
    (Branch List (fromList [15]) List)

testFind :: Assertion
testFind = do
  findElementInTestTree 5 @?= Nothing
  findElementInTestTree 1 @?= Just 1
  findElementInTestTree 4 @?= Just 4
  findElementInTestTree 7 @?= Just 7
  findElementInTestTree 15 @?= Just 15

testInsert :: Assertion
testInsert = do
  insert startTree 6 @?=
    Branch
      (Branch (Branch List (fromList [1]) List) (fromList [4]) (Branch List (fromList [6]) List))
      (fromList [8])
      List
  insert startTree 4 @?=
    Branch (Branch (Branch List (fromList [1]) List) (fromList [4, 4]) List) (fromList [8]) List
  where
    startTree =
      Branch
        (Branch (Branch List (fromList [1 :: Int]) List) (fromList [4]) List)
        (fromList [8])
        List

testFromList :: Assertion
testFromList =
  treeFromList [1, 2, 5, 3, 8, 9, 10, 15, 2, 5, 3, 1, 2, 4, 6, 7, 1, 8] @?=
  Branch
    (Branch
       (Branch (Branch List (fromList [1, 1, 1]) List) (fromList [2, 2, 2]) List)
       (fromList [3, 3])
       (Branch (Branch List (fromList [4]) List) (fromList [5, 5]) List))
    (fromList [6 :: Int])
    (Branch
       (Branch (Branch List (fromList [7]) List) (fromList [8, 8]) List)
       (fromList [9])
       (Branch (Branch List (fromList [10]) List) (fromList [15]) List))

eraseElementFromTestTree :: Int -> Tree Int
eraseElementFromTestTree =
  erase $
  Branch
    (Branch List (fromList [2]) List)
    (fromList [3])
    (Branch
       (Branch List (fromList [5]) (Branch List (fromList [6]) List))
       (fromList [7, 7])
       (Branch List (fromList [10, 10]) List))

testErase :: Assertion
testErase = do
  eraseElementFromTestTree 7 @?=
    Branch
      (Branch List (fromList [2]) List)
      (fromList [3])
      (Branch
         (Branch List (fromList [5]) (Branch List (fromList [6]) List))
         (fromList [7])
         (Branch List (fromList [10, 10]) List))
  eraseElementFromTestTree 2 @?=
    Branch
      List
      (fromList [3])
      (Branch
         (Branch List (fromList [5]) (Branch List (fromList [6]) List))
         (fromList [7, 7])
         (Branch List (fromList [10, 10]) List))
  eraseElementFromTestTree 5 @?=
    Branch
      (Branch List (fromList [2]) List)
      (fromList [3])
      (Branch
         (Branch List (fromList [6]) List)
         (fromList [7, 7])
         (Branch List (fromList [10, 10]) List))
  eraseElementFromTestTree 3 @?=
    Branch
      (Branch List (fromList [2]) List)
      (fromList [5])
      (Branch
         (Branch List (fromList [6]) List)
         (fromList [7, 7])
         (Branch List (fromList [10, 10]) List))

block1Task3TestGroup :: TestTree
block1Task3TestGroup =
  testGroup
    "Block1.Task3"
    [ testCase "Test isEmpty" testEmpty
    , testCase "Test size" testSize
    , testCase "Test find" testFind
    , testCase "Test insert" testInsert
    , testCase "Test fromList" testFromList
    , testCase "Test erase" testErase
    ]
