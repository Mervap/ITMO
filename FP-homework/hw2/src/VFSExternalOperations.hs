{-# LANGUAGE AllowAmbiguousTypes   #-}
{-# LANGUAGE InstanceSigs          #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE OverloadedStrings     #-}

module VFSExternalOperations
  ( initShellState
  , saveVFS
  ) where

import Control.Arrow ((***))
import Control.Monad (join, when)
import Data.ByteString as BS (readFile, writeFile)
import Data.List (foldl', partition)
import Data.Map.Strict as Map (Map, elems, empty, fromList, insert, singleton,
                               union, (!))
import System.Directory

import VFSTypes

initShellState :: FilePath -> IO ShellState
initShellState vfsRootPath = do
  let rootPath = filePathToDirPath vfsRootPath
  allDirs <- readDirectory rootPath
  return $ ShellState rootPath allDirs rootPath (allDirs ! rootPath)

readDirectory :: VFSPath -> IO (Map VFSPath Directory)
readDirectory path = do
  let stringPath = dirPathToFilePath path
  perms <- getPermissions stringPath
  size <- getFileSize stringPath
  modificationTime <- getModificationTime stringPath
  if not $ readable perms
    then return $
         Map.singleton path $
         Directory (parentDir path) (head path) size perms modificationTime Map.empty Map.empty
    else do
      content <- map (path </.>) <$> listDirectory stringPath
      isDirs <- mapM (doesDirectoryExist . dirPathToFilePath) content
      let (dirsPaths, filePaths) = join (***) (map fst) $ partition snd (zip content isDirs)
      dirs <- mapM readDirectory dirsPaths
      files <- mapM getFile filePaths
      let thisDir =
            Directory
              (parentDir path)
              (head path)
              size
              perms
              modificationTime
              (Map.fromList $ map (\x -> (fileName x, x)) files)
              (Map.fromList $ map (\x -> (head x, x)) dirsPaths)
      return $ insert path thisDir (foldl' union empty dirs)

getFile :: VFSPath -> IO File
getFile path = do
  let stringPath = dirPathToFilePath path
  perms <- getPermissions stringPath
  content <-
    if readable perms
      then BS.readFile stringPath
      else return ""
  modificationTime <- getModificationTime stringPath
  size <- getFileSize stringPath
  return $ File (parentDir path) (head path) content size perms modificationTime

saveVFS :: ShellState -> IO ()
saveVFS shell = saveDirectory shell (rootVFSPath shell)

saveDirectory :: ShellState -> VFSPath -> IO ()
saveDirectory shell dPath = do
  let dir = allVFSDirs shell ! dPath
  let dirPath = dirPathToFilePath dPath
  existFile <- doesFileExist dirPath
  existDir <- doesDirectoryExist dirPath
  if existDir
    then do
      nowTime <- getModificationTime dirPath
      when
        (nowTime /= dirModificationTime dir)
        (do nowPerms <- getPermissions dirPath
            if not $ writable nowPerms
              then putStrLn $ "Cannot rewrite dir " ++ dirPath ++ ": dir not writeble \n"
              else updateDirInfo dir dirPath)
    else if existFile
           then putStrLn $ "Cannot create dir " ++ dirPath ++ ": file with such name exist\n"
           else updateDirInfo dir dirPath
  where
    updateDirInfo :: Directory -> FilePath -> IO ()
    updateDirInfo dir dirPath = do
      let files = elems $ dirFiles dir
      let dirs = elems $ dirDirs dir
      createDirectoryIfMissing True dirPath
      mapM_ saveFile files
      mapM_ (saveDirectory shell) dirs

saveFile :: File -> IO ()
saveFile file = do
  let filePath = dirPathToFilePath (fileParentPath file </> [fileName file])
  existFile <- doesFileExist filePath
  existDir <- doesDirectoryExist filePath
  if existFile
    then do
      nowTime <- getModificationTime filePath
      when
        (nowTime /= fileModificationTime file)
        (do nowPerms <- getPermissions filePath
            if not $ writable nowPerms
              then putStrLn $ "Cannot rewrite file " ++ filePath ++ ": file not writeble \n"
              else updateFileInfo filePath)
    else if existDir
           then putStrLn $ "Cannot create file " ++ filePath ++ ": directory with such name exist\n"
           else updateFileInfo filePath
  where
    updateFileInfo :: FilePath -> IO ()
    updateFileInfo filePath = do
      BS.writeFile filePath (fileContent file)
      setModificationTime filePath (fileModificationTime file)
      setPermissions filePath (filePermissions file)
