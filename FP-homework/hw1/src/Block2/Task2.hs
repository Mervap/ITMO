module Block2.Task2 where

import Data.Foldable (foldl', foldr')
import Data.List.NonEmpty (NonEmpty (..))

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
splitOn delim = foldr' folder ([] :| [])
  where
    folder element (h :| hs)
      | element == delim = [] :| (h : hs)
      | otherwise = (element : h) :| hs

joinWith :: a -> NonEmpty [a] -> [a]
joinWith delim (h :| hs) = foldl' (\ans sublist -> ans ++ (delim : sublist)) h hs
