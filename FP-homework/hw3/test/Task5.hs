{-# LANGUAGE ScopedTypeVariables #-}

module Task5
  ( task5TestGroup
  ) where

import System.Directory (getCurrentDirectory)
import System.FilePath ((</>))
import Test.Tasty
import Test.Tasty.HUnit

import Task5.FileSystem
import Task5.Practice

testDirDirs :: Assertion
testDirDirs =
  dirDirs
    (Dir "/" [File "A", Dir "B" [], File "Cc", Dir "D" [Dir "E" [], File "F"]]) @?=
  [Dir "B" [], Dir "D" [Dir "E" [], File "F"]]

testMaybeDirName :: Assertion
testMaybeDirName = do
  maybeDirName (Dir "A" []) @?= Just "A"
  maybeDirName (File "A")   @?= Nothing

testSafeFileName :: Assertion
testSafeFileName = do
  safeFileName (Dir "A" []) @?= ""
  safeFileName (File "A")   @?= "A"

testRootDefName :: Assertion
testRootDefName = do
  rootDefName (Dir "A" [Dir "B" [], File "C"]) @?=
    Dir "/" [Dir "B" [], File "C"]
  rootDefName (File "A") @?= File "A"

testAddRootSuffix :: Assertion
testAddRootSuffix = do
  addRootSuffix "bacaba" (Dir "A" [Dir "B" [], File "C"]) @?=
    Dir "Abacaba" [Dir "B" [], File "C"]
  addRootSuffix "bacaba" (File "A") @?= File "A"

testFirstDirName :: Assertion
testFirstDirName = do
  firstDirName (Dir "A" [Dir "B" [], File "C", Dir "D" []]) @?= Just "B"
  firstDirName (Dir "A" [File "B", File "C"]) @?= Nothing
  firstDirName (File "A") @?= Nothing

testFileNameList :: Assertion
testFileNameList = do
  fileNameList (Dir "A" [File "L", Dir "B" [], File "C", Dir "D" []]) @?= ["L", "C"]
  fileNameList (Dir "A" [Dir "B" [], Dir "D" []]) @?= []
  fileNameList (File "A") @?= []

testFSDirRead :: Assertion
testFSDirRead = do
  wd <- getCurrentDirectory
  dir <- getDirectory (wd </> "testData")
  dir @?=
    Dir
      "testData"
      [ Dir "B" [File "empty"]
      , Dir "Dir" [File "ppddff"]
      , File "someText"
      , File "File.txt"
      ]

task5TestGroup :: TestTree
task5TestGroup =
  testGroup
    "Task5"
    [ testCase "Dir dirs"          testDirDirs
    , testCase "Maybe dir name"    testMaybeDirName
    , testCase "Safe file name"    testSafeFileName
    , testCase "Default root name" testRootDefName
    , testCase "Add root suffix"   testAddRootSuffix
    , testCase "First dir name"    testFirstDirName
    , testCase "File name list"    testFileNameList
    , testCase "File system read"  testFSDirRead
    ]
