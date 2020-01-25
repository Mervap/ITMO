module OpProofs where

import           Data.List       as L
import qualified Data.Map.Strict as Map
import           Types
import           Utils

opProof :: ([String] -> [Expr]) -> BinOp -> Bool -> Bool -> (Bool, [Expr])
---------
-- And --
---------
-- !A already proven
opProof sub And False False =
  ( False
  , sub
      [ "!A -> A & B -> !A"
      , "A & B -> !A"
      , "A & B -> A"
      , "(A & B -> A) -> (A & B -> !A) -> !(A & B)"
      , "(A & B -> !A) -> !(A & B)"
      , "!(A & B)"
      ])
-- !B already proven
opProof sub And True False =
  ( False
  , sub
      [ "!B -> A & B -> !B"
      , "A & B -> !B"
      , "A & B -> B"
      , "(A & B -> B) -> (A & B -> !B) -> !(A & B)"
      , "(A & B -> !B) -> !(A & B)"
      , "!(A & B)"
      ])
-- !A already proven
opProof sub And False True =
  ( False
  , sub
      [ "!A -> A & B -> !A"
      , "A & B -> !A"
      , "A & B -> A"
      , "(A & B -> A) -> (A & B -> !A) -> !(A & B)"
      , "(A & B -> !A) -> !(A & B)"
      , "!(A & B)"
      ])
-- A and B already proven
opProof sub And True True = (True, sub ["A -> B -> A & B", "B -> A & B", "A & B"])
--------
-- Or --
--------
-- !A and !B already proven
opProof sub Or False False =
  ( False
  , sub
      [ "A -> A -> A"                                                                               -- 1. Ax. 1
      , "(A -> (A -> A)) -> (A -> (A -> A) -> A) -> (A -> A)"                                       -- 2. Ax. 2
      , "(A -> A) -> (B -> A) -> ((A | B) -> A)"                                                    -- 3. Ax. 8
      , "((A | B) -> A) -> ((A | B) -> !A) -> !(A | B)"                                             -- 4. Ax. 9
      , "(A -> (A -> A) -> A) -> (A -> A)"                                                          -- 5. Mp. 2, 1
      , "A -> (A -> A) -> A"                                                                        -- 6. Ax. 1
      , "A -> A"                                                                                    -- 7. Mp. 5, 6
      , "(B -> A) -> ((A | B) -> A)"                                                                -- 8. Mp. 3, 7
      , "B -> !A -> B"                                                                              -- 9. Ax. 1
      , "(!B -> !A -> !B) -> B -> (!B -> !A -> !B)"                                                 -- 10. Ax. 1
      , "!B -> !A -> !B"                                                                            -- 11. Ax. 1
      , "B -> (!B -> !A -> !B)"                                                                     -- 12. Mp. 10, 11
      , "!B -> B -> !B"                                                                             -- 13. Ax. 1
      , "B -> !B"                                                                                   -- 14. Mp, 13, ?
      , "(B -> !B) -> (B -> !B -> (!A -> !B)) -> (B -> (!A -> !B))"                                 -- 15. Ax. 2
      , "(B -> !B -> (!A -> !B)) -> (B -> (!A -> !B))"                                              -- 16. Mp. 15, 14
      , "B -> (!A -> !B)"                                                                           -- 17. Mp. 16, 12
      , "((!A -> B) -> (!A -> !B) -> !!A) -> B -> ((!A -> B) -> (!A -> !B) -> !!A)"                 -- 18. Ax. 1
      , "(!A -> B) -> (!A -> !B) -> !!A"                                                            -- 19. Ax. 9
      , "B -> (!A -> B) -> ((!A -> !B) -> !!A)"                                                     -- 20. Mp. 18, 19
      , "(B -> (!A -> B)) -> (B -> (!A -> B) -> ((!A -> !B) -> !!A)) -> (B -> ((!A -> !B) -> !!A))" -- 21. Ax. 2
      , "(B -> (!A -> B) -> ((!A -> !B) -> !!A)) -> (B -> ((!A -> !B) -> !!A))"                     -- 22. Mp. 21, 9
      , "B -> ((!A -> !B) -> !!A)"                                                                  -- 23. Mp. 22, 20
      , "(B -> (!A -> !B)) -> (B -> (!A -> !B) -> !!A) -> (B -> !!A)"                               -- 24. Ax. 2
      , "(B -> (!A -> !B) -> !!A) -> (B -> !!A)"                                                    -- 25. Mp. 24, 11
      , "B -> !!A"                                                                                  -- 26. Mp. 25, 23
      , "(!!A -> A) -> B -> (!!A -> A)"                                                             -- 27. Ax. 1
      , "!!A -> A"                                                                                  -- 28. Ax. 10
      , "B -> (!!A -> A)"                                                                           -- 29. Mp. 27, 28
      , "(B -> !!A) -> (B -> !!A -> A) -> (B -> A)"                                                 -- 30. Ax. 2
      , "(B -> !!A -> A) -> (B -> A)"                                                               -- 31. Mp. 30, 26
      , "B -> A"                                                                                    -- 32. Mp. 31, 29
      , "(A | B) -> A"                                                                              -- 33. Mp. 8, 32
      , "((A | B) -> !A) -> !(A | B)"                                                               -- 34. Mp. 4, 33
      , "!A -> (A | B) -> !A"                                                                       -- 35. Ax. 1
      , "(A | B) -> !A"                                                                             -- 36. Mp. 35, ?
      , "!(A | B)"                                                                                  -- 37. Mp. 34, 36
      ])
-- A already proven
opProof sub Or True False = (True, sub ["A -> A | B", "A | B"])
-- B already proven
opProof sub Or False True = (True, sub ["B -> A | B", "A | B"])
-- A already proven
opProof sub Or True True = (True, sub ["A -> A | B", "A | B"])
----------
-- Impl --
----------
-- !A already proven
opProof sub Impl False False = do
  let context = sub ["!A", "!B", "A"]
  let proof =
        sub
          [ "A"
          , "A -> !(A -> B) -> A"
          , "!(A -> B) -> A"
          , "!A"
          , "!A -> !(A -> B) -> !A"
          , "!(A -> B) -> !A"
          , "(!(A -> B) -> A) -> (!(A -> B) -> !A) -> !!(A -> B)"
          , "(!(A -> B) -> !A) -> !!(A -> B)"
          , "!!(A -> B)"
          , "!!(A -> B) -> (A -> B)"
          , "A -> B"
          , "B"
          ]
  (True, snd $ deduction (context, proof))
-- A and !B already proven
opProof sub Impl True False = do
  let st =
        sub
          [ "!B -> (A -> B) -> !B"
          , "(A -> B) -> !B"
          , "((A -> B) -> A) -> ((A -> B) -> (A -> B)) -> ((A -> B) -> B)"
          , "A -> (A -> B) -> A"
          , "(A -> B) -> A"
          , "((A -> B) -> (A -> B)) -> ((A -> B) -> B)"
          ]
  let mid =
        helper
          [ "A -> (A -> A)"
          , "(A -> (A -> A)) -> (A -> (A -> A) -> A) -> (A -> A)"
          , "(A -> (A -> A) -> A) -> (A -> A)"
          , "A -> (A -> A) -> A"
          , "(A -> A)"
          ]
  let end =
        sub
          [ "(A -> B) -> B"
          , "((A -> B) -> B) -> ((A -> B) -> !B) -> !(A -> B)"
          , "((A -> B) -> !B) -> !(A -> B)"
          , "!(A -> B)"
          ]
  (False, st ++ mid ++ end)
  where
    helper = substitution (Map.singleton "A" (Op Impl (head $ sub ["A"]) (head $ sub ["B"])))
-- B already proven
opProof sub Impl False True = (True, sub ["B -> A -> B", "A -> B"])
-- B already proven
opProof sub Impl True True = (True, sub ["B -> A -> B", "A -> B"])
