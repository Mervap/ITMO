module Task5.FileSystem
  ( -- * File system type
    FS(..)

    -- * Basic @Traversal'@ to accessing the fields
  , contentsList
  , contents
  , dirName
  , fileName
  , getDirectory
  ) where

import Control.Arrow ((***))
import Control.Monad
import Data.List (partition)
import Lens.Micro
import System.Directory
import System.FilePath

-- | Virtual file system type
data FS
  = Dir
      { _name     :: FilePath  -- ^ Directory name (not full path)
      , _contents :: [FS]      -- ^ Directory child intems
      }
  | File
      { _name :: FilePath      -- ^ File name (not full path)
      }
  deriving (Show, Eq)

-- | 'Traversal' for directly '_contents' list
contentsList :: Traversal' FS [FS]
contentsList f (Dir name cont) = Dir name <$> f cont
contentsList _ file            = pure file

-- | 'Traversal' for '_contents'
contents :: Traversal' FS FS
contents f (Dir name cont) = Dir name <$> traverse f cont
contents _ file            = pure file

-- | 'Traversal' for 'Dir' @_name@
dirName :: Traversal' FS FilePath
dirName f (Dir name cont) = Dir <$> f name <*> pure cont
dirName _ file            = pure file

-- | 'Traversal' for 'File' @_name@
fileName :: Traversal' FS FilePath
fileName f (File name) = File <$> f name
fileName _ dir         = pure dir

-- | Compute 'FS' which copies real file system structure under @path@
getDirectory :: FilePath -> IO FS
getDirectory path = do
  perms <- getPermissions path
  let dName = takeFileName path
  if not $ readable perms
    then return $ Dir dName []
    else do
      content <- map (path </>) <$> listDirectory path
      isDirs <- mapM doesDirectoryExist content
      let (dirsPaths, filePaths) =
            join (***) (map fst) $ partition snd (zip content isDirs)
      dirs <- mapM getDirectory dirsPaths
      let files = map (File . takeFileName) filePaths
      return $ Dir dName (dirs ++ files)
