module Task7
  ( task7TestGroup
  ) where

import Lens.Micro
import Test.Tasty
import Test.Tasty.HUnit

import Task5.FileSystem
import Task7.AdvancedSystemOperations

rootDir :: FS
rootDir =
  Dir
    "/"
    [ File "B"
    , File "T"
    , Dir "C" [File "G", File "Z", Dir "LL" [File "P"]]
    , Dir "R" [File "A", Dir "D" []]
    ]

testChangeExt :: Assertion
testChangeExt = do
  changeExt "r" (Dir "/" [File "B.a", File "B.b", Dir "C.l" [File "G.g"]]) @?=
    Dir "/" [File "B.r", File "B.r", Dir "C.l" [File "G.g"]]
  changeExt "r" (File "E.a") @?= File "E.a"

testListNames :: Assertion
testListNames = do
  listNames rootDir @?=
    [ "/"
    , "/B"
    , "/T"
    , "/C/"
    , "/C/G"
    , "/C/Z"
    , "/C/LL/"
    , "/C/LL/P"
    , "/R/"
    , "/R/A"
    , "/R/D/"
    ]
  listNames (Dir "/" []) @?= ["/"]
  listNames (File "E")   @?= ["E"]

testRmDir :: Assertion
testRmDir = do
  let dir = Dir "/" [File "A", Dir "A" [], Dir "B" [File "R"]]
  rmDir "A" dir @?= Dir "/" [File "A", Dir "B" [File "R"]]
  rmDir "B" dir @?= dir
  rmDir "A" (File "A") @?= File "A"

testMoveGetPath :: Assertion
testMoveGetPath = do
  rootDir ^? getPath @?= Just "/"
  rootDir ^? move "C" . move "LL" . getPath @?= Just "/C/LL/"
  rootDir ^? move "R" . move "LL" . getPath @?= Nothing
  rootDir ^? move "C" . move "LL" . move "G" . getPath @?= Nothing
  rootDir ^? move "C" . move "LL" . move "P" . getPath @?= Just "/C/LL/P"

task7TestGroup :: TestTree
task7TestGroup =
  testGroup
    "Task7"
    [ testCase "Change extention"       testChangeExt
    , testCase "List names"             testListNames
    , testCase "Remove empty directory" testRmDir
    , testCase "Move + getPath"         testMoveGetPath
    ]
