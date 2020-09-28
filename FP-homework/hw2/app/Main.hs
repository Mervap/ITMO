{-# LANGUAGE ScopedTypeVariables #-}

module Main where

import Control.Exception.Safe as Safe (onException)
import Control.Monad.Except (runExceptT)
import Control.Monad.Reader (runReaderT)
import Control.Monad.State (runState)
import Control.Monad.Writer.Strict
import qualified Data.ByteString.Char8 as B (putStrLn)
import Data.Char (isSpace)
import Data.Time (getCurrentTime)
import Debug.Trace (trace)
import Options.Applicative.Extra
import System.Console.Readline
import System.Directory
import System.Directory.Internal.Prelude
import System.IO (BufferMode (..), hSetBuffering, stdin, stdout)

import Parsers
import VFSExternalOperations
import VFSTypes

completionFun :: String -> Int -> Int -> IO (Maybe (String, [String]))
completionFun _ _ _ = trace "tap" $ return $ Just ("hell", ["helloWorld"])

main :: IO ()
main = do
  rootPath <- getRootPath
  putStrLn "VFS initialization..." >> hFlush stdout
  fileSystem <- initShellState rootPath
  putStrLn "Promt initialization..." >> hFlush stdout
  initialise
  putStrLn "Done" >> hFlush stdout
  Safe.onException (processNextCommand fileSystem) (saveVFS fileSystem)
  where
    getRootPath :: IO String
    getRootPath = do
      args <- getArgs
      case args of
        []     -> getCurrentDirectory
        path:_ -> return path

initialise :: IO ()
initialise = do
  hSetBuffering stdout NoBuffering
  hSetBuffering stdin NoBuffering
  initialize

getLineEdited :: String -> IO (Maybe String)
getLineEdited prompt = do
  ms <- readline prompt
  case ms of
    Nothing -> return ms
    Just s  -> unless (all isSpace s) (addHistory s) >> return ms

processNextCommand :: ShellState -> IO ()
processNextCommand shell = do
  maybeUnparsedCommand <- getLineEdited $ dirPathToFilePath (workingPath shell) ++ "> "
  case maybeUnparsedCommand of
    Nothing -> saveVFS shell
    Just unparsedCommand -> do
      command <- handleParseResultSafely $ parse unparsedCommand
      case command of
        Just (VFSCmd Exit) -> saveVFS shell
        Just (VFSCmd Save) -> do
          saveVFS shell
          Safe.onException (processNextCommand shell) (saveVFS shell)
        Just p -> do
          time <- getCurrentTime
          let ((res, out), newShell) =
                runState (runWriterT (runReaderT (runExceptT $ process p) time)) shell
          case res of
            Left e -> print e
            Right _ -> do
              B.putStrLn out
              hFlush stdout
          Safe.onException (processNextCommand newShell) (saveVFS newShell)
        Nothing -> do
          hFlush stdout
          Safe.onException (processNextCommand shell) (saveVFS shell)

handleParseResultSafely :: ParserResult a -> IO (Maybe a)
handleParseResultSafely (Failure failure) = do
  let (msg, _) = renderFailure failure ""
  putStrLn msg
  return Nothing
handleParseResultSafely res = Just <$> handleParseResult res
