{-# LANGUAGE Rank2Types #-}
{-# LANGUAGE LambdaCase #-}

module Task7.AdvancedSystemOperations
  ( -- * Advanced file system operations
    changeExt
  , listNames
  , rmDir
  , move
  , getPath
  ) where

import Lens.Micro

import System.FilePath
import Task5.FileSystem

-- | Change all file extensions in the directory to another one (not recursively)
changeExt :: String -> FS -> FS
changeExt ext fs = fs & contents . fileName %~ flip replaceExtensions ext

-- | Names of all files and directories recursively
listNames :: FS -> [FilePath]
listNames fs = fs ^.. listNamesInt (fs ^. dirName)
  where
    listNamesInt :: FilePath -> Traversal' FS FilePath
    listNamesInt path f s@(File _)  = fileName (f . (path </>)) s
    listNamesInt path f s@(Dir _ _) =
      dirName (f . (addTrailingPathSeparator . (path </>))) s *>
      (contents . listNamesInt (path </> s ^. dirName)) f s

-- | Delete the selected subdirectory (only if it is empty)
rmDir :: FilePath -> FS -> FS
rmDir path fs =
  fs & contentsList .~
  (fs ^.. contents .
   filtered (\dir -> not $ (dir ^. dirName == path) && null (dir ^.. contents)))

-- | Move to the selected directory with keeping the path to the root.
-- Excludes the possibility of changing the received object
move :: FilePath -> SimpleFold FS FS
move path =
  folding $ \fs ->
    map
      (rename (fs ^. dirName))
      (fs ^.. contents . filtered (\d -> d ^. fsName == path))
  where
    fsName = failing dirName fileName
    rename pref =
      \case
        (File name) -> File (pref </> name)
        (Dir name con) -> Dir (pref </> name) con

-- | Get path to the root
getPath :: SimpleGetter FS FilePath
getPath =
  to $ \case
    (File name) -> name
    (Dir name _) -> addTrailingPathSeparator name
