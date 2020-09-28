module Block4.Task1
  ( stringSum
  ) where

import Text.Read

stringSum :: String -> Maybe Int
stringSum string = sum <$> traverse readMaybe (words string)
