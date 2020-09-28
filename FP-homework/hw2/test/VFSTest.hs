{-# LANGUAGE InstanceSigs      #-}
{-# LANGUAGE OverloadedStrings #-}

module VFSTest
  ( vfsTestGroup
  ) where

import Data.ByteString.Char8 (ByteString, pack)
import Data.Map.Strict ((!))
import Test.Tasty
import Test.Tasty.HUnit

import TestHelpers
import VFSTypes

testLs :: Assertion
testLs = do
  shell <- initShell
  vfsBaseVoid (Ls "" False) shell rootContent (@?= shell)
  vfsBaseVoid (Ls "B" False) shell bContent (@?= shell)
  newShell <- vfsBase (Cd "B") shell "" (@?= changeDir shell "B")
  vfsBaseVoid (Ls "" False) newShell bContent (@?= newShell)
  vfsBaseVoid (Ls ".." False) newShell rootContent (@?= newShell)

testCd :: Assertion
testCd = do
  shell <- initShell
  newShell <- vfsBase (Cd "B") shell "" (@?= changeDir shell "B")
  newShell1 <- vfsBase (Cd "..") newShell "" (@?= shell)
  vfsBaseVoid (Cd ".") newShell1 "" (@?= newShell1)

testCat :: Assertion
testCat = do
  shell <- initShell
  vfsBaseVoid (Cat "B/B.txt") shell "Multi line\ntext" (@?= shell)
  vfsBaseVoid (Cat "A.txt") shell " aaaa aaa aaaa" (@?= shell)
  let moved = changeDir shell "src"
  vfsBaseVoid (Cat "../B/B.txt") moved "Multi line\ntext" (@?= moved)
  vfsBaseVoid (Cat "some.hs") moved "module Test where" (@?= moved)

testMkDir :: Assertion
testMkDir = do
  shell <- initShell
  vfsBaseVoid (MkDir "C") shell "" (checkDir "C")
  vfsBaseVoid (MkDir "B/C") shell "" (checkDir "B/C")
  let moved = changeDir shell "src"
  vfsBaseVoid (MkDir "./C") moved "" (checkDir "src/C")
  vfsBaseVoid (MkDir "../B/C") moved "" (checkDir "B/C")

testTouch :: Assertion
testTouch = do
  shell <- initShell
  vfsBaseVoid (Touch "C.txt") shell "" (checkFile "C.txt")
  vfsBaseVoid (Touch "B/Cccc.al") shell "" (checkFile "B/Cccc.al")
  let moved = changeDir shell "src"
  vfsBaseVoid (Touch "./file") moved "" (checkFile "src/file")
  vfsBaseVoid (Touch "../B/another") moved "" (checkFile "B/another")

testFind :: Assertion
testFind = do
  shell <- initShell
  vfsBaseVoid (Find "" "B.txt") shell "/B/B.txt\n" (@?= shell)
  vfsBaseVoid (Find "." "B.txt") shell "./B/B.txt\n" (@?= shell)
  vfsBaseVoid (Find "src" "B.txt") shell "" (@?= shell)
  vfsBaseVoid (Find "B" "C.txt") shell "" (@?= shell)
  newShell <- vfsBase (Touch "B/C.txt") shell "" (checkFile "B/C.txt")
  vfsBaseVoid (Find "B" "C.txt") newShell "B/C.txt\n" (@?= newShell)

infoHeader :: ByteString
infoHeader = "Perms  Size     ModificationTime        Type  FullPath"

aInfo :: ShellState -> IO ByteString
aInfo shell = do
  let dir = workingDirectory shell
  let file = dirFiles dir ! "A.txt"
  let time = take 19 $ show $ fileModificationTime file
  let root = dirPathToFilePath $ rootVFSPath shell
  return $
    infoHeader <> "\n" <> "rw-      14  " <> pack time <> "        .txt  " <> pack root <> "/A.txt"

testFileInfo :: Assertion
testFileInfo = do
  shell <- initShell
  info <- aInfo shell
  vfsBaseVoid (Info "A.txt") shell info (@?= shell)

testRmDir :: Assertion
testRmDir = do
  shell <- initShell
  afterCreation <- vfsBase (MkDir "C") shell "" (checkDir "C")
  afterDeletion <- vfsBase (RemoveDir "C") afterCreation "" (checkNoDir "C")
  afterDeletion1 <- vfsBase (RemoveFile "B/newDir/file") afterDeletion "" (checkNoFile "B/newDir/file")
  vfsBaseVoid (RemoveDir "B/newDir") afterDeletion1 "" (checkNoDir "B/newDir")

testRm :: Assertion
testRm = do
  shell <- initShell
  afterCreation <- vfsBase (Touch "C.txt") shell "" (checkFile "C.txt")
  afterDeletion <- vfsBase (RemoveFile "C.txt") afterCreation "" (checkNoFile "C.txt")
  vfsBaseVoid (RemoveFile "B/B.txt") afterDeletion "" (checkNoFile "B/B.txt")

testWrite :: Assertion
testWrite = do
  shell <- initShell
  vfsBaseVoid (Cat "A.txt") shell " aaaa aaa aaaa" (@?= shell)
  afterWrite <- vfsBase (Write "A.txt" "new text") shell "" (const $ return ())
  vfsBaseVoid (Cat "A.txt") afterWrite "new text" (@?= afterWrite)

vfsTestGroup :: TestTree
vfsTestGroup =
  testGroup
    "Vfs tests"
    [ testCase "Test ls" testLs
    , testCase "Test cd" testCd
    , testCase "Test cat" testCat
    , testCase "Test mkdir" testMkDir
    , testCase "Test touch" testTouch
    , testCase "Test find" testFind
    , testCase "Test info" testFileInfo
    , testCase "Test rmdir" testRmDir
    , testCase "Test rm" testRm
    , testCase "Test write" testWrite
    ]
