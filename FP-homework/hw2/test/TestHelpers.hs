{-# LANGUAGE LambdaCase        #-}
{-# LANGUAGE OverloadedStrings #-}

module TestHelpers
  ( initShell
  , rootContent
  , bContent
  , srcContent
  , changeDir
  , emptyCheck
  , checkDir
  , checkFile
  , checkNoDir
  , checkNoFile
  , vfsBaseVoid
  , vfsBase
  , vcsBaseVoid
  , vcsBase
  ) where

import Control.Monad (void)
import Control.Monad.Except (runExceptT)
import Control.Monad.Reader (runReaderT)
import Control.Monad.State.Lazy (runState)
import Control.Monad.Writer.Strict (runWriterT)
import Data.ByteString.Char8 (ByteString)
import Data.Foldable (foldl')
import Data.Map.Strict ((!), (!?))
import Data.Time.Clock.POSIX (getCurrentTime)
import System.Directory (getCurrentDirectory)
import System.FilePath (combine, dropFileName)
import Test.Tasty.HUnit

import Parsers
import VFSExternalOperations (initShellState)
import VFSTypes

initShell :: IO ShellState
initShell = do
  wok <- getCurrentDirectory
  initShellState (combine wok "testDir")

rootContent :: ByteString
rootContent = foldl' (\a b -> a <> b <> "\n") "" ["A.txt", "B", "src"]

bContent :: ByteString
bContent = foldl' (\a b -> a <> b <> "\n") "" ["B.txt", "file.pdf", "newDir"]

srcContent :: ByteString
srcContent = foldl' (\a b -> a <> b <> "\n") "" ["some.hs", "some.qs"]

changeDir :: ShellState -> FilePath -> ShellState
changeDir shell path = do
  let fullpath = workingPath shell </> filePathToDirPath path
  shell {workingPath = fullpath, workingDirectory = allVFSDirs shell ! fullpath}

emptyCheck :: ShellState -> Assertion
emptyCheck = const $ return ()

noErrors :: Either CommandException () -> Assertion
noErrors =
  \case
    Left err -> assertFailure (show err)
    Right _ -> return ()

checkDir :: FilePath -> ShellState -> Assertion
checkDir path shell = do
  let absolutePath = rootVFSPath shell </> filePathToDirPath path
  let allDirs = allVFSDirs shell
  case allDirs !? absolutePath of
    Nothing -> assertFailure $ "No dir " ++ path
    Just _  -> return ()

checkNoDir :: FilePath -> ShellState -> Assertion
checkNoDir path shell = do
  let absolutePath = rootVFSPath shell </> filePathToDirPath path
  let allDirs = allVFSDirs shell
  case allDirs !? absolutePath of
    Nothing -> return ()
    Just _  -> assertFailure $ "Dir exist " ++ path

checkFile :: FilePath -> ShellState -> Assertion
checkFile path shell = do
  let (name:parDir) = rootVFSPath shell </> filePathToDirPath path
  let allDirs = allVFSDirs shell
  case allDirs !? parDir of
    Nothing -> assertFailure $ "No dir " ++ dropFileName path
    Just dir ->
      case dirFiles dir !? name of
        Nothing -> assertFailure $ "No file " ++ path
        Just _  -> return ()

checkNoFile :: FilePath -> ShellState -> Assertion
checkNoFile path shell = do
  let (name:parDir) = rootVFSPath shell </> filePathToDirPath path
  let allDirs = allVFSDirs shell
  case allDirs !? parDir of
    Nothing -> return ()
    Just dir ->
      case dirFiles dir !? name of
        Nothing -> return ()
        Just _  -> assertFailure $ "File exist " ++ path

base :: Command -> ShellState -> ByteString -> (ShellState -> Assertion) -> IO ShellState
base command shell output check = do
  time <- getCurrentTime
  let ((res, out), newFs) =
        runState (runWriterT (runReaderT (runExceptT $ process command) time)) shell
  noErrors res
  out @?= output
  check newFs
  return newFs

vfsBase :: VFSCommand -> ShellState -> ByteString -> (ShellState -> Assertion) -> IO ShellState
vfsBase = base . VFSCmd

vfsBaseVoid :: VFSCommand -> ShellState -> ByteString -> (ShellState -> Assertion) -> IO ()
vfsBaseVoid = (((void .) .) .) . vfsBase

vcsBase :: VCSCommand -> ShellState -> ByteString -> (ShellState -> Assertion) -> IO ShellState
vcsBase = base . CVSCmd

vcsBaseVoid :: VCSCommand -> ShellState -> ByteString -> (ShellState -> Assertion) -> IO ()
vcsBaseVoid = (((void .) .) .) . vcsBase
