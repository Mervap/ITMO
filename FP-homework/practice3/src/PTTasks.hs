module PTTasks where

import Data.List.Split

-- Task 12
newtype Path a = Path {unPath :: [String]}
  deriving Show

data Abs -- для абсолютного пути
data Rel -- для относительного

createAbs :: String -> Maybe (Path Abs)
createAbs ('/':hs) = Just $ Path (splitOn "/" hs)
createAbs _ = Nothing

createRel :: String -> Maybe (Path Rel)
createRel ('.':'/':[]) = Nothing
createRel ('.':'/':hs) = Just $ Path (splitOn "/" hs)

