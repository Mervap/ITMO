{-# LANGUAGE ScopedTypeVariables #-}

module Task6
  ( task6TestGroup
  ) where

import Lens.Micro
import Test.Tasty
import Test.Tasty.HUnit

import Task5.FileSystem
import Task6.SystemOperations

rootDir :: FS
rootDir =
  Dir
    "/"
    [ Dir "A" [File "R", Dir "B" [File "C", File "Z", Dir "K" []]]
    , File "F"
    , Dir "B" [File "C"]
    ]

testCd :: Assertion
testCd = do
  rootDir  ^? cd "A"  @?= 
    Just (Dir "A" [File "R", Dir "B" [File "C", File "Z", Dir "K" []]])
  rootDir  ^? cd "E"  @?= Nothing
  File "E" ^? cd "E"  @?= Nothing

testLs :: Assertion
testLs = do
  rootDir    ^.. ls @?= ["A", "F", "B"]
  Dir "D" [] ^.. ls @?= []
  File "F"   ^.. ls @?= []

testFilename :: Assertion
testFilename = do
  rootDir  ^? file "F" @?= Just "F"
  rootDir  ^? file "E" @?= Nothing
  File "F" ^? file "F" @?= Just "F"
  File "F" ^? file "E" @?= Nothing

testCdFile :: Assertion
testCdFile = do
  rootDir ^? cd "A" . cd "B" . file "C" @?= Just "C"
  rootDir ^? cd "A" . cd "B" . file "L" @?= Nothing
  rootDir ^? cd "B" . cd "C" . file "R" @?= Nothing

testCdLs :: Assertion
testCdLs = do
  rootDir ^.. cd "A" . cd "B" . ls          @?= ["C", "Z", "K"]
  rootDir ^.. cd "A" . cd "R" . ls          @?= []
  rootDir ^.. cd "A" . cd "B" . cd "K" . ls @?= []

task6TestGroup :: TestTree
task6TestGroup =
  testGroup
    "Task6"
    [ testCase "Cd"            testCd
    , testCase "Ls"            testLs
    , testCase "Filename"      testFilename
    , testCase "Cd + filename" testCdFile
    , testCase "Cd + ls"       testCdLs
    ]
