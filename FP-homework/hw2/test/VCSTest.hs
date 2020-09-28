{-# LANGUAGE InstanceSigs      #-}
{-# LANGUAGE OverloadedStrings #-}

module VCSTest
  ( vcsTestGroup
  ) where

import Data.ByteString (ByteString)
import Data.ByteString.Char8 (pack)
import System.FilePath ((</>))
import Test.Tasty
import Test.Tasty.HUnit

import TestHelpers
import VersionControlSystem
import VFSTypes hiding ((</>))

testVCSInit :: Assertion
testVCSInit = do
  shell <- initShell
  vcsBaseVoid (Init "") shell "" (checkDir vcsDirName)
  vcsBaseVoid (Init "src") shell "" (checkDir $ "src" </> vcsDirName)

testVCSAdd :: Assertion
testVCSAdd = do
  shell <- initShell
  afterInitial <- vcsBase (Init "") shell "" (checkDir vcsDirName)
  afterAdd <- vcsBase (Add "A.txt") afterInitial "" (checkDir $ vcsDirName </> "A.txt")
  let versionPath = vcsDirName </> "A.txt" </> "0"
  let commentPath = versionPath <> vcsCommentSuffix
  checkFile versionPath afterAdd
  checkFile commentPath afterAdd
  vfsBaseVoid (Cat versionPath) afterAdd " aaaa aaa aaaa" emptyCheck
  vfsBaseVoid (Cat commentPath) afterAdd "initial" emptyCheck

testVCSCommit :: Assertion
testVCSCommit = do
  shell <- initShell
  afterInitial <- vcsBase (Init "") shell "" emptyCheck
  afterAdd <- vcsBase (Add "A.txt") afterInitial "" emptyCheck
  afterWrite <- vfsBase (Write "A.txt" "hehehe") afterAdd "" emptyCheck
  afterCommit <- vcsBase (Commit "A.txt" "Commit message") afterWrite "" emptyCheck
  let versionPath = vcsDirName </> "A.txt" </> "1"
  let commentPath = versionPath <> vcsCommentSuffix
  checkFile versionPath afterCommit
  checkFile commentPath afterCommit
  vfsBaseVoid (Cat versionPath) afterCommit "hehehe" emptyCheck
  vfsBaseVoid (Cat commentPath) afterCommit "Commit message" emptyCheck

historyMessage :: ShellState -> ByteString
historyMessage shell = do
  let root = pack $ dirPathToFilePath $ rootVFSPath shell
  "VCS root: " <> root <> "/B" <> "\n" <> "  0. initial" <> "\n" <> "  1. Hohoho commit" <>
    "\nVCS root: " <>
    root <>
    "\n" <>
    "  0. initial" <>
    "\n" <>
    "  1. Hohoho commit" <>
    "\n"

testVCSHistory :: Assertion
testVCSHistory = do
  shell <- initShell
  afterInitial' <- vcsBase (Init ".") shell "" emptyCheck
  afterInitial <- vcsBase (Init "B") afterInitial' "" emptyCheck
  afterAdd <- vcsBase (Add "B/B.txt") afterInitial "" emptyCheck
  afterWrite <- vfsBase (Write "B/B.txt" "Hohoho") afterAdd "" emptyCheck
  afterCommit <- vcsBase (Commit "B/B.txt" "Hohoho commit") afterWrite "" emptyCheck
  vcsBaseVoid (History "B/B.txt") afterCommit (historyMessage afterCommit) emptyCheck

testVCSCatVersion :: Assertion
testVCSCatVersion = do
  shell <- initShell
  afterInitial' <- vcsBase (Init "") shell "" emptyCheck
  afterAdd' <- vcsBase (Add "B/B.txt") afterInitial' "" emptyCheck
  afterWrite <- vfsBase (Write "B/B.txt" "Hohoho") afterAdd' "" emptyCheck
  afterInitial <- vcsBase (Init "B") afterWrite "" emptyCheck
  afterAdd <- vcsBase (Add "B/B.txt") afterInitial "" emptyCheck
  afterCommit <- vcsBase (Commit "B/B.txt" "Hohoho commit") afterAdd "" emptyCheck
  vcsBaseVoid (CatVersion "B/B.txt" 0 0) afterCommit "Hohoho" emptyCheck
  vcsBaseVoid (CatVersion "B/B.txt" 1 0) afterCommit "Hohoho" emptyCheck
  vcsBaseVoid (CatVersion "B/B.txt" 0 1) afterCommit "Multi line\ntext" emptyCheck
  vcsBaseVoid (CatVersion "B/B.txt" 1 1) afterCommit "Hohoho" emptyCheck

historyMessage' :: ShellState -> ByteString
historyMessage' shell = do
  let root = pack $ dirPathToFilePath $ rootVFSPath shell
  "VCS root: " <> root <> "/B" <> "\n" <> "  1. Hihihi commit" <> "\nVCS root: " <> root <> "\n" <>
    "  0. initial" <>
    "\n" <>
    "  1. Hihihi commit" <>
    "\n"

testVCSRemoveVersion :: Assertion
testVCSRemoveVersion = do
  shell <- initShell
  afterInitial' <- vcsBase (Init "B") shell "" emptyCheck
  afterInitial <- vcsBase (Init ".") afterInitial' "" emptyCheck
  afterAdd <- vcsBase (Add "B/B.txt") afterInitial "" emptyCheck
  afterWrite <- vfsBase (Write "B/B.txt" "Hihihi") afterAdd "" emptyCheck
  afterCommit <- vcsBase (Commit "B/B.txt" "Hihihi commit") afterWrite "" emptyCheck
  afterDelete <- vcsBase (RemoveVersion "B/B.txt" 0 0) afterCommit "" emptyCheck
  let versionPath = "B" </> vcsDirName </> "B.txt" </> "0"
  let commentPath = versionPath <> vcsCommentSuffix
  checkNoFile versionPath afterDelete
  checkNoFile commentPath afterDelete
  vcsBaseVoid (History "B/B.txt") afterDelete (historyMessage' afterDelete) emptyCheck

testVCSRemoveAllVersions :: Assertion
testVCSRemoveAllVersions = do
  shell <- initShell
  afterInitial <- vcsBase (Init "B") shell "" emptyCheck
  afterAdd <- vcsBase (Add "B/B.txt") afterInitial "" emptyCheck
  afterWrite <- vfsBase (Write "B/B.txt" "Hehehe") afterAdd "" emptyCheck
  afterCommit <- vcsBase (Commit "B/B.txt" "Hehehe commit") afterWrite "" emptyCheck
  vcsBaseVoid
    (RemoveAllVersions "B/B.txt")
    afterCommit
    ""
    (checkNoDir $ "B" </> vcsDirName </> "B.txt")

historyMessage'' :: ShellState -> ByteString
historyMessage'' shell = do
  let root = pack $ dirPathToFilePath $ rootVFSPath shell
  "VCS root: " <> root <> "/B" <> "\n" <> "  0. initial\n>>>>>\nHohoho commit" <> "\n" <>
    "  2. Hihihi commit" <>
    "\n"

testVCSMergeVersions :: Assertion
testVCSMergeVersions = do
  shell <- initShell
  afterInitial <- vcsBase (Init "B") shell "" emptyCheck
  afterAdd <- vcsBase (Add "B/file.pdf") afterInitial "" emptyCheck
  afterWrite <- vfsBase (Write "B/file.pdf" "Hohoho") afterAdd "" emptyCheck
  afterCommit <- vcsBase (Commit "B/file.pdf" "Hohoho commit") afterWrite "" emptyCheck
  afterWrite1 <- vfsBase (Write "B/file.pdf" "Hihihi") afterCommit "" emptyCheck
  afterCommit1 <- vcsBase (Commit "B/file.pdf" "Hihihi commit") afterWrite1 "" emptyCheck
  afterMerge <-
    vcsBase
      (Merge "B/file.pdf" 0 1 Both 0)
      afterCommit1
      ""
      (checkNoDir $ "B" </> vcsDirName </> "B.txt")
  let versionPath = "B" </> vcsDirName </> "file.pdf" </> "1"
  let commentPath = versionPath <> vcsCommentSuffix
  checkNoFile versionPath afterMerge
  checkNoFile commentPath afterMerge
  vcsBaseVoid (CatVersion "B/file.pdf" 0 0) afterMerge "Some text\n\n>>>>>\nHohoho" emptyCheck
  vcsBaseVoid (CatVersion "B/file.pdf" 2 0) afterMerge "Hihihi" emptyCheck
  vcsBaseVoid (History "B/file.pdf") afterMerge (historyMessage'' afterMerge) emptyCheck

allHistoryMessage :: ShellState -> ByteString
allHistoryMessage shell = do
  let root = pack $ dirPathToFilePath $ rootVFSPath shell
  root <> "/B/B.txt:" <> "\n" <> "  0. initial\n" <> root <> "/B/file.pdf:" <> "\n" <>
    "  0. initial\n"

testVCSAllHistory :: Assertion
testVCSAllHistory = do
  shell <- initShell
  afterDeletion <- vfsBase (RemoveFile "B/newDir/file") shell "" (checkNoFile "B/newDir/file")
  afterInitial <- vcsBase (Init "") afterDeletion "" emptyCheck
  afterAdd <- vcsBase (Add "B") afterInitial "" emptyCheck
  vcsBaseVoid (ShowAllHistory 0) afterAdd (allHistoryMessage afterAdd) emptyCheck

vcsTestGroup :: TestTree
vcsTestGroup =
  testGroup
    "Vcs tests"
    [ testCase "Test init" testVCSInit
    , testCase "Test add" testVCSAdd
    , testCase "Test commit" testVCSCommit
    , testCase "Test history" testVCSHistory
    , testCase "Test cat" testVCSCatVersion
    , testCase "Test del ver" testVCSRemoveVersion
    , testCase "Test del all" testVCSRemoveAllVersions
    , testCase "Test merge" testVCSMergeVersions
    , testCase "Test all history" testVCSAllHistory
    ]
