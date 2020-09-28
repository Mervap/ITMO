{-# LANGUAGE OverloadedStrings #-}

module VFSCommand
  ( processVFSCommand
  , createNewDirectory
  , updateWorkingDirectory
  , createNewFile
  , removeFile
  , removeDirectory
  , addFileContent
  , writeFile
  ) where

import Prelude hiding (writeFile)

import Control.Monad (void)
import Control.Monad.Except
import Control.Monad.Reader (ask)
import Control.Monad.State
import Control.Monad.Writer.Strict
import Data.ByteString as BS (empty, length)
import Data.ByteString.Char8 (ByteString, pack)
import Data.List as List (isPrefixOf, length, sort)
import Data.Map.Strict as Map (assocs, delete, elems, empty, insert, keys, size,
                               (!), (!?))
import System.Directory.Internal (Permissions (..), writable)
import System.FilePath (addTrailingPathSeparator, combine, takeExtension)

import VFSTypes

defaultPermissions :: Permissions
defaultPermissions = Permissions True True False False

processVFSCommand :: VFSCommand -> Process ()
processVFSCommand (Ls path isLong) =
  processPath
    path
    (showDirectoryInfo isLong)
    (\_ fPath -> throwError $ NoSuchDirectory fPath "is not a directory")
    (\_ dPath nm -> throwError $ NoSuchDirectory (dPath </> [nm]) "directory doesn't exist")
processVFSCommand (Cd path) =
  processPath
    path
    updateWorkingDirectory
    (\_ fPath -> throwError $ NoSuchDirectory fPath "is not a directory")
    (\_ dPath nm -> throwError $ NoSuchDirectory (dPath </> [nm]) "directory doesn't exist")
processVFSCommand (Cat path) =
  processPath
    path
    (\_ dPath -> throwError $ NoSuchFile dPath "is not a file")
    (flip $ const addFileContent)
    (\_ dPath nm -> throwError $ NoSuchFile (dPath </> [nm]) "file doesn't exist")
processVFSCommand (MkDir path) =
  processPath
    path
    (\_ dPath -> throwError $ CannotCreateDirectory dPath "such directory already exist")
    (\_ fPath -> throwError $ CannotCreateDirectory fPath "such file already exist")
    (((void .) .) . createNewDirectory)
processVFSCommand (Touch path) =
  processPath
    path
    (\_ dPath -> throwError $ CannotCreateFile dPath "such directory already exist")
    (\_ fPath -> throwError $ CannotCreateFile fPath "such file already exist")
    (((void .) .) . createNewFile)
processVFSCommand (Find path name) =
  processPath
    path
    (\dir _ -> findInDir (addTrailingPathSeparator path) dir)
    (\_ fPath -> throwError $ NoSuchDirectory fPath "is not a directory")
    (\_ dPath nm -> throwError $ NoSuchDirectory (dPath </> [nm]) "directory doesn't exist")
  where
    findInDir :: FilePath -> Directory -> Process ()
    findInDir prefix dir = do
      let filenames = keys $ dirFiles dir
      let dirs = assocs $ dirDirs dir
      mapM_ (tellFind prefix) filenames
      allDirs <- gets allVFSDirs
      mapM_
        (\(dName, dirPath) ->
           tellFind prefix dName >> findInDir (combine prefix dName) (allDirs ! dirPath))
        dirs
    tellFind :: FilePath -> String -> Process ()
    tellFind prefix elemName =
      when (name `isPrefixOf` elemName) $ tell (pack (combine prefix elemName) <> "\n")
processVFSCommand (Info path) =
  processPath
    path
    (\dir dPath -> addFileInfoHeader >> addDirectoryInfo dir dPath)
    (\file fPath -> addFileInfoHeader >> addFileInfo file fPath)
    (\_ dPath nm -> throwError $ NoSuchFile (dPath </> [nm]) "file doesn't exist")
processVFSCommand (RemoveDir path) =
  processPath
    path
    removeDirectory
    (\_ fPath -> throwError $ CannotRemoveDirectory fPath "is not a directory")
    (\_ dPath name -> throwError $ CannotRemoveDirectory (dPath </> [name]) "no such directory")
processVFSCommand (RemoveFile path) =
  processPath
    path
    (\_ dPath -> throwError $ CannotRemoveFile dPath "is not a file")
    removeFile
    (\_ dPath name -> throwError $ CannotRemoveFile (dPath </> [name]) "no such file")
processVFSCommand (Write path text) =
  processPath
    path
    (\_ dPath -> throwError $ CannotWriteToFile dPath "is not a file")
    (writeFile text)
    (\_ dPath nm -> throwError $ CannotWriteToFile (dPath </> [nm]) "file doesn't exist")
processVFSCommand _ = return ()

showDirectoryInfo :: Bool -> Directory -> VFSPath -> Process ()
showDirectoryInfo isLong dir dPath = do
  let dirNames = keys $ dirDirs dir
  let fileNames = keys $ dirFiles dir
  if isLong
    then do
      addFileInfoHeader
      allDirs <- gets allVFSDirs
      mapM_ (\pth -> addDirectoryInfo (allDirs ! pth) pth) (elems $ dirDirs dir)
      mapM_ (\file -> addFileInfo file (dPath </> [fileName file])) (elems $ dirFiles dir)
    else mapM_ (tell . pack . (++ "\n")) (sort $ dirNames ++ fileNames)

updateWorkingDirectory :: Directory -> VFSPath -> Process ()
updateWorkingDirectory dir dPath =
  modify $ \shell -> shell {workingDirectory = dir, workingPath = dPath}

addFileContent :: File -> Process ()
addFileContent file = tell (fileContent file)

createNewDirectory :: Directory -> VFSPath -> VFSName -> Process Directory
createNewDirectory parDir dPath name = do
  time <- ask
  if not $ writable (dirPermissions parDir)
    then throwError $ CannotCreateDirectory dPath "Permission denied"
    else do
      let dir = Directory dPath name 0 defaultPermissions time Map.empty Map.empty
      allDirs <- gets allVFSDirs
      let parentDirs = dirDirs parDir
      let dirPath = dPath </> [name]
      let newDir = parDir {dirDirs = insert name dirPath parentDirs}
      modify
        (\fs -> do
           let modifiedAllDirs = insert dirPath dir (insert dPath newDir allDirs)
           fs {allVFSDirs = modifiedAllDirs})
      updateSizeAndTime dPath
      return dir

createNewFile :: Directory -> VFSPath -> VFSName -> Process File
createNewFile parDir dPath name = do
  time <- ask
  let file = File dPath name BS.empty 0 defaultPermissions time
  let parentFiles = dirFiles parDir
  allDirs <- gets allVFSDirs
  let newParentDir = parDir {dirFiles = insert name file parentFiles}
  modify (\fs -> fs {allVFSDirs = insert dPath newParentDir allDirs})
  updateSizeAndTime dPath
  return file

removeDirectory :: Directory -> VFSPath -> Process ()
removeDirectory dir dPath
  | not $ writable (dirPermissions dir) =
    throwError $ CannotRemoveDirectory dPath "not enough permissions"
  | size (dirFiles dir) + size (dirDirs dir) /= 0 =
    throwError $ CannotRemoveDirectory dPath "directory is not empty"
  | otherwise = do
    wrkPath <- gets workingPath
    if wrkPath == dPath
      then throwError $ CannotRemoveDirectory dPath "cannot remove wotking directory"
      else do
        allDirs <- gets allVFSDirs
        let parentPath = dirParentPath dir
        let parDir = allDirs ! parentPath
        let newParDir = parDir {dirDirs = delete (dirName dir) (dirDirs parDir)}
        modify
          (\fs -> do
             let modifiedAllDirs = delete dPath (insert parentPath newParDir allDirs)
             fs {allVFSDirs = modifiedAllDirs})
        updateSizeAndTime parentPath

removeFile :: File -> VFSPath -> Process ()
removeFile file fPath
  | not $ writable (filePermissions file) =
    throwError $ CannotRemoveFile fPath "not enough permissions"
  | otherwise = do
    let parentPath = fileParentPath file
    allDirs <- gets allVFSDirs
    let parDir = allDirs ! parentPath
    let newParDir = parDir {dirFiles = delete (fileName file) (dirFiles parDir)}
    modify
      (\fs -> do
         let modifiedAllDirs = delete fPath (insert parentPath newParDir allDirs)
         fs {allVFSDirs = modifiedAllDirs})
    updateSizeAndTime parentPath

writeFile :: ByteString -> File -> VFSPath -> Process ()
writeFile text file fPath
  | not $ writable (filePermissions file) =
    throwError $ CannotWriteToFile fPath "not enough permissions"
  | otherwise = do
    let filelen = toInteger $ BS.length text
    time <- ask
    let newFile = file {fileContent = text, fileSize = filelen, fileModificationTime = time}
    allDirs <- gets allVFSDirs
    let parentPath = fileParentPath file
    let parDir = allDirs ! parentPath
    let parentFiles = dirFiles parDir
    let newParentDir = parDir {dirFiles = insert (fileName file) newFile parentFiles}
    modify (\fs -> fs {allVFSDirs = insert parentPath newParentDir allDirs})
    updateSizeAndTime parentPath

updateSizeAndTime :: VFSPath -> Process ()
updateSizeAndTime path = do
  time <- ask
  allDirs <- gets allVFSDirs
  wrkPath <- gets workingPath
  case allDirs !? path of
    Nothing -> return ()
    Just dir -> do
      let newDir = dir {dirModificationTime = time}
      modify
        (\fs -> do
           let modifiedAllDirs = insert path newDir allDirs
           fs {allVFSDirs = modifiedAllDirs, workingDirectory = modifiedAllDirs ! wrkPath})
      updateSizeAndTime (dirParentPath newDir)

addFileInfoHeader :: Process ()
addFileInfoHeader = tell "Perms  Size     ModificationTime        Type  FullPath"

addFileInfo :: File -> VFSPath -> Process ()
addFileInfo file path = do
  tell "\n"
  renderPermission $ filePermissions file
  tell "  "
  tell $ pack $ spacePrefix 6 $ show (fileSize file)
  tell "  "
  tell $ pack $ take 19 $ show (fileModificationTime file)
  tell "  "
  let name = fileName file
  let ext = takeExtension name
  tell $
    pack $
    spacePrefix 10 $
    if ext == ""
      then "unknown"
      else ext
  tell "  "
  tell $ pack $ dirPathToFilePath path

spacePrefix :: Int -> String -> String
spacePrefix n text = replicate (n - List.length text) ' ' ++ text

renderPermission :: Permissions -> Process ()
renderPermission (Permissions r w e _) = do
  renderOpt r "r"
  renderOpt w "w"
  renderOpt e "e"
  where
    renderOpt :: Bool -> String -> Process ()
    renderOpt p ok =
      tell $
      pack $
      if p
        then ok
        else "-"

addDirectoryInfo :: Directory -> VFSPath -> Process ()
addDirectoryInfo dir path = do
  tell "\n"
  renderPermission $ dirPermissions dir
  tell "  "
  tell $ pack $ spacePrefix 6 $ show (dirSize dir)
  tell "  "
  tell $ pack $ take 19 $ show (dirModificationTime dir)
  tell "  "
  tell $ pack $ spacePrefix 10 "dir"
  tell "  "
  tell $ pack $ dirPathToFilePath path
