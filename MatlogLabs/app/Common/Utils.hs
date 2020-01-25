module Utils where

import           Data.List       as L
import qualified Data.Map.Strict as Map
import           Data.Maybe
import           Lexer           (alexScanTokens)
import           Parser          (parser)
import           Types

type MapState = (Map.Map Expr Int, Map.Map Expr (Int, Int), Map.Map Expr [(Int, Expr)])

emptyMapState :: MapState
emptyMapState = (Map.empty, Map.empty, Map.empty)

contextToOut :: [Expr] -> Expr -> String
contextToOut [] d     = "|- " ++ show d
contextToOut [h] d    = show h ++ " " ++ contextToOut [] d
contextToOut (h:hs) d = show h ++ ", " ++ contextToOut hs d

aImplB :: Expr -> (Expr, [Expr])
aImplB (Op Impl a b) =
  ( Op Impl (Not b) (Not a)
  , substitution
      (Map.fromList [("A", a), ("B", b)])
      [ "(A -> B) -> (!B -> (A -> B))"
      , "!B -> (A -> B)"
      , "!B -> (A -> !B)"
      , "(A -> B) -> ((A -> !B) -> !A)"
      , "((A -> B) -> ((A -> !B) -> !A)) -> (!B -> ((A -> B) -> ((A -> !B) -> !A)))"
      , "!B -> ((A -> B) -> ((A -> !B) -> !A))"
      , "(!B -> (A -> B)) -> ((!B -> ((A -> B) -> ((A -> !B) -> !A))) -> (!B -> ((A -> !B) -> !A)))"
      , "(!B -> ((A -> B) -> ((A -> !B) -> !A))) -> (!B -> ((A -> !B) -> !A))"
      , "!B -> ((A -> !B) -> !A)"
      , "(!B -> (A -> !B)) -> ((!B -> ((A -> !B) -> !A)) -> (!B -> !A))"
      , "(!B -> ((A -> !B) -> !A)) -> (!B -> !A)"
      , "!B -> !A"
      ])
aImplB l = error ("Not specified for expr " ++ show l)

checkAx :: Bool -> Expr -> Maybe Int
checkAx isIntuit = checkAxHelper
  where
    checkAxHelper (Op Impl a (Op Impl b c))
      | a == c = Just 1
    checkAxHelper (Op Impl (Op Impl a b) (Op Impl (Op Impl c (Op Impl d e)) (Op Impl f g)))
      | a == c && a == f && b == d && e == g = Just 2
    checkAxHelper (Op Impl a (Op Impl b (Op And c d)))
      | a == c && b == d = Just 3
    checkAxHelper (Op Impl (Op And a b) c)
      | a == c = Just 4
    checkAxHelper (Op Impl (Op And a b) c)
      | b == c = Just 5
    checkAxHelper (Op Impl a (Op Or b c))
      | a == b = Just 6
    checkAxHelper (Op Impl a (Op Or b c))
      | a == c = Just 7
    checkAxHelper (Op Impl (Op Impl a b) (Op Impl (Op Impl c d) (Op Impl (Op Or e f) g)))
      | a == e && c == f && b == d && b == g = Just 8
    checkAxHelper (Op Impl (Op Impl a b) (Op Impl (Op Impl c (Not d)) (Not e)))
      | a == c && a == e && b == d = Just 9
    checkAxHelper (Op Impl (Not (Not a)) b)
      | not isIntuit && a == b = Just 10
    checkAxHelper (Op Impl a (Op Impl (Not b) c))
      | isIntuit && a == b = Just 10
    checkAxHelper _ = Nothing

checkHyp :: [Expr] -> Expr -> Maybe Int
checkHyp h e =
  case elemIndex e h of
    (Just i) -> Just (i + 1)
    Nothing  -> Nothing

checkMP :: Map.Map Expr (Int, Int) -> Expr -> Maybe (Int, Int)
checkMP map x = Map.lookup x map

makeAnswer :: Bool -> [Expr] -> Map.Map Expr (Int, Int) -> Expr -> Maybe AnnotatedExpr
makeAnswer isIntuit context map x =
  case checkAx isIntuit x of
    (Just a) -> Just (Ax a x)
    Nothing ->
      case checkHyp context x of
        (Just a) -> Just (Hyp a x)
        Nothing ->
          case checkMP map x of
            (Just (a, b)) -> Just (MP a b x)
            Nothing       -> Nothing --error (show context ++ " " ++ show x)

addToMp :: Int -> (Int, Expr) -> Map.Map Expr (Int, Int) -> Map.Map Expr (Int, Int)
addToMp num (n, r) = Map.insert r (n, num)

newMMp1 :: Expr -> Int -> Map.Map Expr [(Int, Expr)] -> Map.Map Expr (Int, Int) -> Map.Map Expr (Int, Int)
newMMp1 e num mR mMp =
  case Map.lookup e mR of
    (Just a) -> foldr (addToMp num) mMp a
    Nothing  -> mMp

newMMp2 :: Expr -> Int -> Map.Map Expr Int -> Map.Map Expr (Int, Int) -> Map.Map Expr (Int, Int)
newMMp2 (Op Impl l r) num mNum mMp =
  case Map.lookup l mNum of
    (Just i) -> Map.insert r (num, i) mMp
    Nothing  -> mMp
newMMp2 _ _ _ m = m

newMR :: Expr -> Int -> Map.Map Expr [(Int, Expr)] -> Map.Map Expr [(Int, Expr)]
newMR (Op Impl l r) num m =
  case Map.lookup l m of
    (Just a) -> Map.insert l ((num, r) : a) m
    Nothing  -> Map.insert l [(num, r)] m
newMR _ _ m = m

annotate :: Bool -> [Expr] -> Int -> Expr -> MapState -> (Maybe (Int, AnnotatedExpr), MapState)
annotate isIntuit context num input state = do
  let (mNum, mMp, mR) = state
  let ans = makeAnswer isIntuit context mMp input
  if isNothing ans
    then (Nothing, state)
    else do
      let new_mNum = Map.insert input num mNum
      let new_mR = newMR input num mR
      let new_mMP = newMMp2 input num mNum (newMMp1 input num mR mMp)
      (Just (num, fromJust ans), (new_mNum, new_mMP, new_mR))

annotateProof :: [Expr] -> [Expr] -> Maybe [(Int, AnnotatedExpr)]
annotateProof = annotateAllProof False

annotateIntuitProof :: [Expr] -> [Expr] -> Maybe [(Int, AnnotatedExpr)]
annotateIntuitProof = annotateAllProof True

annotateAllProof :: Bool -> [Expr] -> [Expr] -> Maybe [(Int, AnnotatedExpr)]
annotateAllProof isIntuit context = helper emptyMapState 1
  where
    helper _ _ [] = Just []
    helper state num (h:hs) =
      case ans of
        Nothing -> Nothing
        (Just ann) ->
          case tail of
            Nothing  -> Nothing
            (Just t) -> Just (ann : t)
      where
        (ans, newState) = annotate isIntuit context num h state
        tail = helper newState (num + 1) hs

annotateCorrectProof :: Bool -> [Expr] -> [Expr] -> [(Int, AnnotatedExpr)]
annotateCorrectProof b c e =
  case annotateAllProof b c e of
    Nothing    -> error "Proof is not correct"
    (Just ans) -> ans

substitution :: Map.Map String Expr -> [String] -> [Expr]
substitution map = L.map (helper . parser . alexScanTokens)
  where
    helper (Op op a b) = Op op (helper a) (helper b)
    helper (Not a) = Not (helper a)
    helper (Var a) =
      case Map.lookup a map of
        Nothing  -> error ("Variable " ++ a ++ " not in the map")
        (Just e) -> e

deduction :: ([Expr], [Expr]) -> ([Expr], [Expr])
deduction = deductionHelper False

deductionIntuit :: ([Expr], [Expr]) -> ([Expr], [Expr])
deductionIntuit = deductionHelper True

deductionHelper :: Bool ->  ([Expr], [Expr]) -> ([Expr], [Expr])
deductionHelper isIntuit (context, proof) = (init context, newProof)
  where
    anProof = L.map snd (annotateCorrectProof isIntuit context proof)
    map = Map.fromList $ zip [1 ..] proof
    newProof = concatMap transformation anProof
    lastHyp = last context
    --
    transformation :: AnnotatedExpr -> [Expr]
    transformation (Hyp _ expr)
      | expr == lastHyp =
        substitution
          (Map.fromList [("A", expr)])
          [ "A -> A -> A" -- Ax. 1
          , "A -> (A -> A) -> A" -- Ax. 1
          , "(A -> A -> A) -> (A -> (A -> A) -> A) -> (A -> A)" -- Ax. 2
          , "(A -> (A -> A) -> A) -> (A -> A)" -- Mp. 3, 1
          , "A -> A" -- Mp. 4, 2
          ]
      | otherwise = helper expr
    transformation (Ax _ expr) = helper expr
    transformation (MP a b expr)
      | Just left <- Map.lookup b map =
        substitution
          (Map.fromList [("A", lastHyp), ("L", left), ("R", expr)])
          ["(A -> L) -> (A -> L -> R) -> (A -> R)", "(A -> L -> R) -> (A -> R)", "A -> R"]
    --
    helper expr =
      substitution
        (Map.fromList [("A", expr), ("B", lastHyp)])
        [ "A" -- Hyp. ?
        , "A -> B -> A" -- Ax. 1
        , "B -> A" -- Mp. 2, 1
        ]
