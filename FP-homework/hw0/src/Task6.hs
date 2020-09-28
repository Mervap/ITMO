module Task6
  (
    -- * Exercises on weak head normal form.
    subtask1
  , whnfSubtask1
  , subtask2
  , whnfSubtask2
  ) where

import Task1 (distributivity)

-- | Subtask 1 exactly.
subtask1 :: (Either String b, Either String c)
subtask1 = distributivity (Left ("harold" ++ " hide " ++ "the " ++ "pain"))

-- | Whnf form of 'subtask1'. A constructor on top level.
--   therefore no further reduction is applied
whnfSubtask1 :: (Either String b, Either String c)
whnfSubtask1 = (Left ("harold" ++ " hide " ++ "the " ++ "pain"),
                Left ("harold" ++ " hide " ++ "the " ++ "pain"))

foo :: Char -> Maybe Double
foo char =
  case char == 'o' of
    True  -> Just $ exp pi
    False -> Nothing

-- not actually, but good enough for this task
myNull :: [a] -> Bool
myNull [] = True
myNull _  = False

mapMaybe :: (a -> Maybe b) -> [a] -> [b]
mapMaybe _ [] = []
mapMaybe f (x:xs) =
  let rs = mapMaybe f xs
   in case f x of
        Nothing -> rs
        Just r  -> r : rs

-- | Subtask 2 exactly.
subtask2 :: Bool
subtask2 = myNull $ mapMaybe foo "pole chudes ochen' chudesno"

-- | Whnf form of 'subtask2'.
whnfSubtask2 :: Bool
whnfSubtask2 = False
