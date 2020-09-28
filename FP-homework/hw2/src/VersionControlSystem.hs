{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeApplications  #-}

module VersionControlSystem
  ( processVCS
  , vcsDirName
  , vcsCommentSuffix
  ) where

import Prelude hiding (writeFile)

import Control.Lens (element, (^?))
import Control.Monad.Except
import Control.Monad.State (gets)
import Control.Monad.Writer (tell)
import Data.ByteString.Char8 (ByteString, pack)
import Data.Char (isDigit)
import Data.List (elemIndex, isSuffixOf, sort)
import Data.Map.Strict (assocs, elems, keys, (!), (!?))
import Data.Maybe (fromJust, isJust, isNothing)

import VFSCommand (addFileContent, createNewDirectory, createNewFile,
                   removeDirectory, removeFile, writeFile)
import VFSTypes

vcsDirName :: String
vcsDirName = ".hw2vcs"

vcsCommentSuffix :: String
vcsCommentSuffix = "-comment"

processVCS :: VCSCommand -> Process ()
processVCS (Init path) =
  processPath
    path
    (\dir dPath -> void (createNewDirectory dir dPath vcsDirName) `catchError` handler)
    (\_ fPath -> throwError $ NoSuchDirectory fPath "is not a directory")
    (\_ dPath nm -> throwError $ NoSuchDirectory (dPath </> [nm]) "directory doesn't exist")
  where
    handler :: CommandException -> Process ()
    handler CannotCreateDirectory {} = throwError InitError
    handler e                        = throwError e
processVCS (Add path) = processVCSRoots ISDirOk NAdded path (updateFileVCS "initial")
processVCS (Commit path comment) = processVCSRoots ISDirOk Added path (updateFileVCS comment)
processVCS (History path) =
  processVCSRoots DirNotOk Added path (const $ \root -> addVCSHeader root >> addFileHistory root)
processVCS (CatVersion path ind rootInd) =
  processVCSRoots
    DirNotOk
    (Index rootInd)
    path
    (const $ \root -> do
       allDirs <- gets allVFSDirs
       case dirFiles (allDirs ! root) !? show ind of
         Nothing  -> throwError $ NoSuchFile [] "bad vers ind"
         Just fll -> addFileContent fll)
processVCS (RemoveVersion path ind rootInd) =
  processVCSRoots
    DirNotOk
    (Index rootInd)
    path
    (const $ \root -> do
       allDirs <- gets allVFSDirs
       let fName = show ind
       let files = dirFiles (allDirs ! root)
       case files !? fName of
         Nothing -> throwError $ NoSuchFile [] "bad vers ind"
         Just file -> do
           removeFile file (root </.> fName)
           let commentName = fName ++ vcsCommentSuffix
           removeFile (files ! commentName) (root </.> commentName))
processVCS (RemoveAllVersions path) =
  processVCSRoots
    ISDirOk
    Added
    path
    (const $ \root -> do
       allDirs <- gets allVFSDirs
       let files = assocs $ dirFiles (allDirs ! root)
       mapM_ (\(fName, file) -> removeFile file (root </.> fName)) files
       newAllDirs <- gets allVFSDirs
       removeDirectory (newAllDirs ! root) root)
processVCS (Merge path il ir mt rootInd) =
  processVCSRoots
    DirNotOk
    (Index rootInd)
    path
    (const $ \root -> do
       allDirs <- gets allVFSDirs
       let files = dirFiles (allDirs ! root)
       case files !? show il of
         Nothing -> throwError $ NoSuchFile [] ("bad vers ind " ++ show il)
         Just fl1 ->
           case files !? show ir of
             Nothing -> throwError $ NoSuchFile [] ("bad vers ind " ++ show ir)
             Just fl2 -> do
               let names = sort $ filter (all isDigit) $ keys files
               let indl = fromJust $ elemIndex (show il) names
               let indr = fromJust $ elemIndex (show ir) names
               if indl + 1 /= indr
                 then throwError $ NoSuchFile [] (show il ++ " not prev version of " ++ show ir)
                 else do
                   let contenL = fileContent fl1
                   let contenR = fileContent fl2
                   let commentFName = show il ++ vcsCommentSuffix
                   let commentFile = files ! commentFName
                   let commentL = fileContent commentFile
                   let commentR = fileContent (files ! (show ir ++ vcsCommentSuffix))
                   mergeConflicts (fl1, root </.> show il, contenL) contenR mt
                   mergeConflicts (commentFile, root </.> commentFName, commentL) commentR mt
                   processVCS (RemoveVersion path ir rootInd))
processVCS (ShowAllHistory rootInd) =
  processVCSRoots
    ISDirOk
    (Index rootInd)
    ""
    (\file root -> do
       tell $ pack $ dirPathToFilePath (fileParentPath file </> [fileName file])
       tell ":\n"
       addFileHistory root)

mergeConflicts :: (File, VFSPath, ByteString) -> ByteString -> MergeStrategy -> Process ()
mergeConflicts (lFile, lPath, contentL) contentR mt =
  when
    (contentL /= contentR)
    (case mt of
       LeftPriority -> return ()
       RightPriority -> writeFile contentR lFile lPath
       Both -> writeFile (contentL <> "\n>>>>>\n" <> contentR) lFile lPath)

collectVCS :: VFSPath -> VFSPath -> Process [VFSPath]
collectVCS path curDir = do
  let vcsRoot = curDir </> [vcsDirName]
  allDirs <- gets allVFSDirs
  fromParent <-
    do root <- gets rootVFSPath
       if root /= curDir
         then collectVCS ([head curDir] </> path) (parentDir curDir)
         else return []
  case allDirs !? vcsRoot of
    Nothing -> return fromParent
    Just _  -> return $ vcsRoot </> path : fromParent

recursiveCreateIfNeeded :: VFSPath -> Process Directory
recursiveCreateIfNeeded path = do
  allDirs <- gets allVFSDirs
  case allDirs !? path of
    Just dir -> return dir
    Nothing -> do
      let (dName:parDirPath) = path
      parDir <- recursiveCreateIfNeeded parDirPath
      createNewDirectory parDir parDirPath dName

updateFileVCS :: ByteString -> File -> VFSPath -> Process ()
updateFileVCS comment file root = do
  pDir <- recursiveCreateIfNeeded root
  let lastRev = maximum $ (-1) : map (read @Integer) (filter (all isDigit) $ keys (dirFiles pDir))
  let nextRev = lastRev + 1
  let fName = show nextRev
  let fCommentName = fName ++ vcsCommentSuffix
  createAndWrite fName (fileContent file)
  createAndWrite fCommentName comment
  where
    createAndWrite :: VFSName -> ByteString -> Process ()
    createAndWrite fName text = do
      allDirs <- gets allVFSDirs
      let pDir = allDirs ! root
      fl <- createNewFile pDir root fName
      writeFile text fl (root </.> fName)

addVCSHeader :: VFSPath -> Process ()
addVCSHeader root = do
  let rootVCS = drop 1 $ dropWhile (/= vcsDirName) root
  tell "VCS root: "
  tell $ pack $ dirPathToFilePath rootVCS ++ "\n"

addFileHistory :: VFSPath -> Process ()
addFileHistory root = do
  allDirs <- gets allVFSDirs
  let pDir = allDirs ! root
  let comments = filter (isSuffixOf vcsCommentSuffix . fileName) $ elems (dirFiles pDir)
  mapM_
    (\f -> tell $ "  " <> pack (takeWhile (/= '-') (fileName f)) <> ". " <> fileContent f <> "\n")
    comments

data VCSRootType
  = Added
  | NAdded
  | Index Int

data IsDirOk
  = ISDirOk
  | DirNotOk
  deriving (Eq)

processVCSRoots ::
     IsDirOk -> VCSRootType -> FilePath -> (File -> VFSPath -> Process ()) -> Process ()
processVCSRoots dr rt path processOnRoot =
  processPath
    path
    (\_ dPath ->
       case dr of
         ISDirOk  -> processDir dPath
         DirNotOk -> throwError $ NoSuchFile [] "dirNotOk")
    (\file _ -> processFile file)
    (\_ dPath nm -> throwError $ NoSuchFile (dPath </> [nm]) "file doesn't exist")
  where
    processFile :: File -> Process ()
    processFile file = do
      allDirs <- gets allVFSDirs
      roots <- collectVCS [fileName file] (fileParentPath file)
      filtered <-
        case rt of
          Added -> return $ filter (isJust . (allDirs !?)) roots
          NAdded -> return $ filter (isNothing . (allDirs !?)) roots
          Index ind -> do
            let added = filter (isJust . (allDirs !?)) roots
            case added ^? element ind of
              Nothing   -> return []
              Just root -> return [root]
      if null filtered && dr /= ISDirOk
        then throwError $ NoSuchFile [] "no such repos"
        else mapM_ (processOnRoot file) filtered
    processDir :: VFSPath -> Process ()
    processDir dPath = do
      allDirs <- gets allVFSDirs
      let dir = allDirs ! dPath
      mapM_ processFile (dirFiles dir)
      let dirsNotVcs = filter ((/= vcsDirName) . fst) $ assocs $ dirDirs dir
      mapM_ (processDir . snd) dirsNotVcs
