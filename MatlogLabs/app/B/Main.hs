module Main where

import           Data.List
import qualified Data.Map.Strict as Map
import           Data.Maybe
import           Lexer           (alexScanTokens)
import           Parser          (parser)
import           ParserContext   (parserC)
import           System.IO
import           Types
import           Utils

data Pair = Pair
  { getId    :: Int
  , getValue :: Expr
  }

instance Eq Pair where
  (Pair a b) == (Pair c d) = b == d

instance Ord Pair where
  (Pair a b) <= (Pair c d) = b <= d
  (Pair a b) < (Pair c d) = b < d

inMap :: Expr -> MapState -> Bool
inMap input state = do
  let (mNum, _, _) = state
  Map.member input mNum

getAnswers :: [Expr] -> MapState -> Expr -> Int -> Bool -> IO (Maybe [(Int, AnnotatedExpr)])
getAnswers context state d num f = do
  eof <- isEOF
  --eof <- hIsEOF h
  if eof
    then return (Just [])
    else do
      i <- getLine
      --i <- hGetLine h
      eof <- isEOF
      --eof <- hIsEOF h
      let input = parser $ alexScanTokens i
      let newF = f || input == d
      if eof && input /= d
        then return Nothing
        else if inMap input state
               then getAnswers context state d num newF
               else do
                 let (ans, newState) = annotate False context num input state
                 if isNothing ans
                   then return Nothing
                   else do
                     result <- getAnswers context newState d (num + 1) newF
                     if isNothing result
                       then return Nothing
                       else if f
                              then return (Just [])
                              else return $ Just (fromJust ans : fromJust result)

getMPMap :: [(Int, AnnotatedExpr)] -> Map.Map Int Int
getMPMap = helper
  where
    helper :: [(Int, AnnotatedExpr)] -> Map.Map Int Int
    helper [] = Map.empty
    helper ((i, MP a b c):hs) = Map.insert a (la + 1) (Map.insert b (lb + 1) map)
      where
        map = helper hs
        la =
          case Map.lookup a map of
            Nothing  -> 0
            (Just i) -> i
        lb =
          case Map.lookup b map of
            Nothing  -> 0
            (Just i) -> i
    helper (h:hs) = helper hs

removeRedundantMP :: Map.Map Int Int -> [(Int, AnnotatedExpr)] -> [(Int, AnnotatedExpr)]
removeRedundantMP map res = snd (helper map res)
  where
    helper :: Map.Map Int Int -> [(Int, AnnotatedExpr)] -> (Map.Map Int Int, [(Int, AnnotatedExpr)])
    helper map [] = (map, [])
    helper map [h] = (map, [h])
    helper map (h@(i, MP a b c):hs) =
      if not (Map.member i newMap) || newMap Map.! i == 0
        then (Map.insert a (newMap Map.! a - 1) (Map.insert b (newMap Map.! b - 1) newMap), t)
        else (newMap, h : t)
      where
        (newMap, t) = helper map hs
    helper map (h:hs) =
      if not (Map.member (fst h) newMap) || newMap Map.! fst h == 0
        then (newMap, t)
        else (newMap, h : t)
      where
        (newMap, t) = helper map hs

reNum :: Int -> [(Int, AnnotatedExpr)] -> [(Int, AnnotatedExpr)]
reNum size res = helper Map.empty res 1
  where
    helper :: Map.Map Int Int -> [(Int, AnnotatedExpr)] -> Int -> [(Int, AnnotatedExpr)]
    helper map [] _ = []
    helper map ((id, MP a b e):hs) ind =
      (ind, MP (map Map.! a) (map Map.! b) e) : helper (Map.insert id ind map) hs (ind + 1)
    helper map ((id, e):hs) ind = (ind, e) : helper (Map.insert id ind map) hs (ind + 1)

toOut :: (Int, AnnotatedExpr) -> String
toOut (id, Ax a e) = "[" ++ show id ++ ". Ax. sch. " ++ show a ++ "] " ++ show e
toOut (id, Hyp a e) = "[" ++ show id ++ ". Hypothesis " ++ show a ++ "] " ++ show e
toOut (id, MP a b e) = "[" ++ show id ++ ". M.P. " ++ show a ++ ", " ++ show b ++ "] " ++ show e

main :: IO ()
main
  --x <- openFile "D:/ITMO/MatlogLabs/app/FirstB/1" ReadMode
  --i <- hGetLine x
 = do
  i <- getLine
  let (context, d) = parserC (alexScanTokens i)
  input <- getAnswers context emptyMapState d 1 False
  --y <- openFile "D:/ITMO/MatlogLabs/app/FirstB/10new.out" WriteMode
  if isNothing input || null (fromJust input)
    then putStrLn "Proof is incorrect"
    else do
      let res = fromJust input
      let result1 = reNum (length res) (removeRedundantMP (getMPMap res) res)
      --hPutStrLn y (contextToOut context d)
      putStrLn (contextToOut context d)
      --mapM_ (hPutStrLn y . toOut) result1
      --hFlush y
      mapM_ (putStrLn . toOut) result1
