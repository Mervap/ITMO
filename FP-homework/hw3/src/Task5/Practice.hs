module Task5.Practice
  ( -- * Some easy operations using lenses
    dirDirs
  , maybeDirName
  , safeFileName
  , rootDefName
  , addRootSuffix
  , firstDirName
  , fileNameList
  ) where

import Lens.Micro

import Task5.FileSystem

-- | List of subtrees of the directory for 'Dir', otherwise an empty list
dirDirs :: FS -> [FS]
dirDirs fs = fs ^.. contents . filtered (has dirName)

-- | 'Maybe' with the name of the directory if 'Dir', 'Nothing' otherwise
maybeDirName :: FS -> Maybe FilePath
maybeDirName fs = fs ^? dirName

-- | The filename, if 'File', an empty string otherwise
safeFileName :: FS -> FilePath
safeFileName fs = fs ^. fileName

-- | Change the name of the tree root to @/@
rootDefName :: FS -> FS
rootDefName fs = fs & dirName .~ "/"

-- | Add an arbitrary suffix to the root name of the tree
addRootSuffix :: String -> FS -> FS
addRootSuffix suf fs = fs & dirName %~ (++ suf)

-- | The name of the first directory in the list of subdirectories.
-- 'Nothing', if there is no such folder
firstDirName :: FS -> Maybe FilePath
firstDirName fs = fs ^? contents . dirName

-- | List of 'File' names only from 'Dir' (non-recursively)
fileNameList :: FS -> [FilePath]
fileNameList fs = fs ^.. contents . fileName
