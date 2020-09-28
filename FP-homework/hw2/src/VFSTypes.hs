module VFSTypes
  ( VFSCommand(..)
  , MergeStrategy(..)
  , VCSCommand(..)
  , Command(..)
  , VFSPath
  , VFSName
  , parentDir
  , dirPathToFilePath
  , filePathToDirPath
  , (</.>)
  , (</>)
  , Directory(..)
  , File(..)
  , ShellState(..)
  , Process
  , processPath
  , CommandException(..)
  ) where

import Control.Monad.Except
import Control.Monad.Reader
import Control.Monad.State
import Control.Monad.Writer.Strict
import Data.ByteString.Char8 (ByteString)
import Data.Map.Strict (Map, (!?))
import Data.Time.Clock (UTCTime)
import System.Directory (Permissions)
import System.FilePath (joinPath, splitDirectories)

data VFSCommand
  = Ls FilePath Bool
  | Cat FilePath
  | Cd FilePath
  | MkDir FilePath
  | Touch FilePath
  | Info FilePath
  | Find FilePath VFSName
  | RemoveDir FilePath
  | RemoveFile FilePath
  | Write FilePath ByteString
  | Save
  | Exit

data MergeStrategy
  = LeftPriority
  | RightPriority
  | Both

data VCSCommand
  = Init FilePath
  | Add FilePath
  | Commit FilePath ByteString
  | History FilePath
  | CatVersion FilePath Int Int
  | RemoveVersion FilePath Int Int
  | RemoveAllVersions FilePath
  | Merge FilePath Int Int MergeStrategy Int
  | ShowAllHistory Int

data Command
  = VFSCmd VFSCommand
  | CVSCmd VCSCommand

data CommandException
  = NoSuchPath VFSPath
  | ExitExcept
  | CannotCreateDirectory VFSPath String
  | CannotCreateFile VFSPath String
  | NoSuchFile VFSPath String
  | NoSuchDirectory VFSPath String
  | CannotRemoveDirectory VFSPath String
  | CannotRemoveFile VFSPath String
  | CannotWriteToFile VFSPath String
  | InitError

instance Show CommandException where
  show (NoSuchPath path) = "Path doesn't exist: " ++ dirPathToFilePath path
  show (CannotCreateDirectory path cause) =
    "Cannot create directory " ++ dirPathToFilePath path ++ ": " ++ cause
  show (CannotCreateFile path cause) =
    "Cannot create file " ++ dirPathToFilePath path ++ ": " ++ cause
  show (CannotRemoveDirectory path cause) =
    "Cannot remove directory " ++ dirPathToFilePath path ++ ": " ++ cause
  show (CannotRemoveFile path cause) =
    "Cannot remove file " ++ dirPathToFilePath path ++ ": " ++ cause
  show (CannotWriteToFile path cause) =
    "Cannot write to file " ++ dirPathToFilePath path ++ ": " ++ cause
  show (NoSuchFile path cause) = "No such file " ++ dirPathToFilePath path ++ ": " ++ cause
  show (NoSuchDirectory path cause) =
    "No such directory " ++ dirPathToFilePath path ++ ": " ++ cause
  show ExitExcept = "Exit was produced"
  show InitError = "Init failed"

type VFSName = String

type VFSPath = [VFSName]

parentDir :: VFSPath -> VFSPath
parentDir = tail

filePathToDirPath :: FilePath -> VFSPath
filePathToDirPath = reverse . splitDirectories

dirPathToFilePath :: VFSPath -> FilePath
dirPathToFilePath = joinPath . reverse

(</.>) :: VFSPath -> String -> VFSPath
dirPath </.> filename = filename : dirPath

(</>) :: VFSPath -> VFSPath -> VFSPath
parentPath </> relativePath = combine parentPath (reverse relativePath)
  where
    combine :: VFSPath -> [VFSName] -> VFSPath
    combine _ x@("/":_)              = reverse x
    combine x []                     = x
    combine x (".":relative)         = combine x relative
    combine (_:path) ("..":relative) = combine path relative
    combine parent (name:child)      = combine (name : parent) child

data Directory =
  Directory
    { dirParentPath       :: VFSPath
    , dirName             :: VFSName
    , dirSize             :: Integer
    , dirPermissions      :: Permissions
    , dirModificationTime :: UTCTime
    , dirFiles            :: Map VFSName File
    , dirDirs             :: Map VFSName VFSPath
    }
  deriving (Show, Eq)

data File =
  File
    { fileParentPath       :: VFSPath
    , fileName             :: VFSName
    , fileContent          :: ByteString
    , fileSize             :: Integer
    , filePermissions      :: Permissions
    , fileModificationTime :: UTCTime
    }
  deriving (Eq)

instance Show File where
  show (File path name _ _ perms modTime) =
    "[" ++ dirPathToFilePath (path </.> name) ++ ", " ++ show perms ++ ", " ++ show modTime ++ "]"

data ShellState =
  ShellState
    { rootVFSPath      :: VFSPath
    , allVFSDirs       :: Map VFSPath Directory
    , workingPath      :: VFSPath
    , workingDirectory :: Directory
    }
  deriving (Show, Eq)

type Process = ExceptT CommandException (ReaderT UTCTime (WriterT ByteString (State ShellState)))

processPath ::
     FilePath
  -> (Directory -> VFSPath -> Process ())
  -> (File -> VFSPath -> Process ())
  -> (Directory -> VFSPath -> String -> Process ())
  -> Process ()
processPath path ifDirExists ifFileExists nothingExist = do
  vfs <- get
  let absolutePath = workingPath vfs </> filePathToDirPath path
  let parentDirPath = parentDir absolutePath
  let name = head absolutePath
  case allVFSDirs vfs !? absolutePath of
    Just dir -> ifDirExists dir absolutePath
    Nothing ->
      case allVFSDirs vfs !? parentDirPath of
        Nothing -> throwError $ NoSuchDirectory parentDirPath "directory doenst't exist"
        Just realWorkingDir ->
          case dirFiles realWorkingDir !? name of
            Just file -> ifFileExists file absolutePath
            Nothing   -> nothingExist realWorkingDir parentDirPath name
