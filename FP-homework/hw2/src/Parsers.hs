module Parsers
  ( parse
  , process
  ) where

import Options.Applicative

import VersionControlSystem
import VFSCommand
import VFSTypes

parse :: String -> ParserResult Command
parse text = execParserPure defaultPrefs opts (words text)
  where
    opts =
      info
        (helper <*> commandParser)
        (fullDesc <> header "Pocket shell" <>
         progDesc "Pocket shell with virtual file system and version control system")

vfsCommand :: String -> Parser VFSCommand -> String -> Mod CommandFields Command
vfsCommand commandName parser desc = command commandName (info (VFSCmd <$> parser) (progDesc desc))

lsCommand :: Mod CommandFields Command
lsCommand =
  vfsCommand
    "ls"
    (Ls <$> strArgument (value "" <> metavar "DIRPATH" <> help "path to dir") <*>
     switch (short 'l' <> help "if specified shows full information"))
    "Displays all subdirectories and files."

cdCommand :: Mod CommandFields Command
cdCommand =
  vfsCommand
    "cd"
    (Cd <$> strArgument (metavar "DIRPATH" <> help "path to dir"))
    "Changes the working directory to the specified."

catCommand :: Mod CommandFields Command
catCommand =
  vfsCommand
    "cat"
    (Cat <$> strArgument (metavar "FILEPATH" <> help "path to file"))
    "Prints contents of file to the console."

mkdirCommand :: Mod CommandFields Command
mkdirCommand =
  vfsCommand
    "mkdir"
    (MkDir <$> strArgument (metavar "DIRPATH" <> help "path to dir"))
    "Creates a new directory along the specified path. The parent directory must exist."

touchCommand :: Mod CommandFields Command
touchCommand =
  vfsCommand
    "touch"
    (Touch <$> strArgument (metavar "FILEPATH" <> help "path to file"))
    "Creates a new file along the specified path. The parent directory must exist."

fileInfoCommand :: Mod CommandFields Command
fileInfoCommand =
  vfsCommand
    "info"
    (Info <$> strArgument (metavar "(FILENAME | DIRPATH)" <> help "path to dir or file"))
    "Outputs full information about the file or directory at the specified path."

findCommand :: Mod CommandFields Command
findCommand =
  vfsCommand
    "find"
    (Find <$> strArgument (value "" <> metavar "DIRPATH" <> help "path to dir") <*>
     strArgument (value "" <> metavar "FILENAME" <> help "File name prefix you are looking for"))
    "Searches for all files in the directory and its subdirectories with the specified name prefix."

rmdirCommand :: Mod CommandFields Command
rmdirCommand =
  vfsCommand
    "rmdir"
    (RemoveDir <$> strArgument (metavar "DIRPATH" <> help "path to dir"))
    "Deletes directory along the specified path. The directory must be empty."

rmCommand :: Mod CommandFields Command
rmCommand =
  vfsCommand
    "rm"
    (RemoveFile <$> strArgument (metavar "FILEPATH" <> help "path to file"))
    "Deletes file along the specified path."

writeCommand :: Mod CommandFields Command
writeCommand =
  vfsCommand
    "write"
    (Write <$> strArgument (metavar "FILENAME" <> help "path to file") <*>
     strArgument (metavar "TEXT" <> help "Text which need to write into file"))
    "Writes the specified text to a file at the specified path. The file must exist."

saveCommand :: Mod CommandFields Command
saveCommand = vfsCommand "save" (pure Save) "Saves changes of virtual file system in real"

exitCommand :: Mod CommandFields Command
exitCommand = vfsCommand "exit" (pure Exit) "Saves changes of virtual file system in real and exit"

vcsCommand :: String -> Parser VCSCommand -> String -> Mod CommandFields Command
vcsCommand commandName parser desc = command commandName (info (CVSCmd <$> parser) (progDesc desc))

vcsInitCommand :: Mod CommandFields Command
vcsInitCommand =
  vcsCommand
    "init"
    (Init <$> strArgument (value "" <> metavar "DIRPATH" <> help "path to the dir"))
    "Initializes the vcs in specified directory."

vcsAddCommand :: Mod CommandFields Command
vcsAddCommand =
  vcsCommand
    "add"
    (Add <$> strArgument (metavar "(FILEPATH | DIRPATH)" <> help "path to the dir or file"))
    "Adds the specified file or directory to all vcs on the way to the vfs root."

vcsCommitCommand :: Mod CommandFields Command
vcsCommitCommand =
  vcsCommand
    "commit"
    (Commit <$> strArgument (metavar "FILEPATH" <> help "path to the file") <*>
     strArgument (metavar "COMMENT" <> help "comment of commit"))
    "Commit changes of specified file to all tracked vcs on the way to the vfs root with specified comment."

vcsHistoryCommand :: Mod CommandFields Command
vcsHistoryCommand =
  vcsCommand
    "hist"
    (History <$> strArgument (metavar "FILEPATH" <> help "path to the file"))
    "Show history of changes of specified file of all tracked vcs on the way to the vfs root with comments."

vscCatCommand :: Mod CommandFields Command
vscCatCommand =
  vcsCommand
    "cat"
    (CatVersion <$> strArgument (metavar "FILEPATH" <> help "path to the file") <*>
     argument auto (metavar "VERSION" <> help "version number") <*>
     argument
       auto
       (value (0 :: Int) <> metavar "VCS_INDEX" <>
        help "index of vcs. The count goes from the file to the vfs root."))
    "Prints a specific version of the file in one of the tracked vcs. By default, the nearest one is searched for."

vcsRmVerCommand :: Mod CommandFields Command
vcsRmVerCommand =
  vcsCommand
    "rmver"
    (RemoveVersion <$> strArgument (metavar "FILEPATH" <> help "path to the file") <*>
     argument auto (metavar "VERSION" <> help "version number") <*>
     argument
       auto
       (value (0 :: Int) <> metavar "VCS_INDEX" <>
        help "index of vcs. The count goes from the file to the vfs root."))
    "Remove a specific version of the file in one of the tracked vcs. By default, the nearest one is searched for."

vcsRmAllCommand :: Mod CommandFields Command
vcsRmAllCommand =
  vcsCommand
    "rmall"
    (RemoveAllVersions <$> strArgument (metavar "FILEPATH" <> help "path to the file"))
    "Deletes all saved file changes in all tracked files vcs."

mergeStrategyLeft :: Parser MergeStrategy
mergeStrategyLeft = flag' LeftPriority (long "left" <> help "Left priority merge.")

mergeStrategyRight :: Parser MergeStrategy
mergeStrategyRight = flag' RightPriority (long "right" <> help "Right priority merge.")

mergeStrategyBoth :: Parser MergeStrategy
mergeStrategyBoth = flag' Both (long "both" <> help "Both merge.")

mergeStrategyParser :: Parser MergeStrategy
mergeStrategyParser = mergeStrategyLeft <|> mergeStrategyRight <|> mergeStrategyBoth

vcsMergeCommand :: Mod CommandFields Command
vcsMergeCommand =
  vcsCommand
    "merge"
    (Merge <$> strArgument (metavar "FILEPATH" <> help "path to the file") <*>
     argument auto (metavar "LEFT_VERSION" <> help "left version number") <*>
     argument auto (metavar "RIGHT_VERSION" <> help "right version number") <*>
     mergeStrategyParser <*>
     argument
       auto
       (value (0 :: Int) <> metavar "VCS_INDEX" <>
        help "index of vcs. The count goes from the file to the vfs root."))
    "Combines two neighboring versions in the file history in the specified vcs. By default, the nearest one is searched for."

cvsShowCommand :: Mod CommandFields Command
cvsShowCommand =
  vcsCommand
    "showall"
    (ShowAllHistory <$>
     argument
       auto
       (value (0 :: Int) <> metavar "VCS_INDEX" <>
        help "index of vcs. The count goes from the file to the vfs root."))
    "Shows the entire history of changes to all files specified vcs. By default, the nearest one is searched for."

cvsParser :: Parser Command
cvsParser =
  hsubparser
    (vcsInitCommand <>
     vcsAddCommand <>
     vcsCommitCommand <>
     vcsHistoryCommand <>
     vscCatCommand <>
     vcsRmVerCommand <>
     vcsRmAllCommand <>
     vcsMergeCommand <>
     cvsShowCommand)

cvsGroupCommand :: Mod CommandFields Command
cvsGroupCommand =
  command "vcs" (info cvsParser (progDesc "Operations for working with version control system."))

commandParser :: Parser Command
commandParser =
  hsubparser
    (lsCommand <>
     cdCommand <>
     catCommand <>
     mkdirCommand <>
     touchCommand <>
     findCommand <>
     fileInfoCommand <>
     rmdirCommand <>
     rmCommand <>
     writeCommand <>
     cvsGroupCommand <>
     saveCommand <>
     exitCommand)

process :: Command -> Process ()
process (VFSCmd vfs) = processVFSCommand vfs
process (CVSCmd cvs) = processVCS cvs
