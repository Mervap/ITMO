{-# LANGUAGE Rank2Types #-}

module Task6.SystemOperations
  ( -- * File system operations
    cd
  , ls
  , file
  ) where

import Lens.Micro

import Task5.FileSystem

-- | Move to the subdirectory with the specified @name@
cd :: FilePath -> Traversal' FS FS
cd name = contents . filtered (\el -> (el ^. dirName) == name)

-- | List of directory content names
ls :: Traversal' FS FilePath
ls = contents . failing dirName fileName

-- | Name of a specific File, if it exists
file :: FilePath -> Traversal' FS FilePath
file name =
  failing
    (fileName . filtered (== name))
    (contents . filtered (\el -> (el ^. fileName) == name) . fileName)
