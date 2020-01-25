module Main where

import           Data.List       as L
import qualified Data.Map.Strict as Map
import           Data.Maybe
import           Lexer           (alexScanTokens)
import           OpProofs
import           Parser          (parser)
import           System.IO
import           Types
import           Utils

evalBool :: Expr -> Map.Map String Bool -> Bool
evalBool (Var e) map       = map Map.! e
evalBool (Not e) map       = not (evalBool e map)
evalBool (Op And a b) map  = evalBool a map && evalBool b map
evalBool (Op Or a b) map   = evalBool a map || evalBool b map
evalBool (Op Impl a b) map = not (evalBool a map && not (evalBool b map))

getVars :: Expr -> [String]
getVars (Var e)     = [e]
getVars (Not e)     = getVars e
getVars (Op op a b) = nub (getVars a ++ getVars b)

checkContext :: Expr -> [String] -> [String] -> Bool -> Bool
checkContext e vars good b = do
  let h = zip good (repeat b)
  let bad = L.filter (`L.notElem` good) vars
  let alf = [False, True]
  let any = L.null [1 | x <- alf, y <- alf, z <- alf, b /= evalBool e (Map.fromList (h ++ zip bad [x, y, z]))]
  any

getContext :: Expr -> Bool -> Maybe [String]
getContext e b = do
  let vars = getVars e
  let zero = [[] | checkContext e vars [] b]
  let one = [[x] | x <- vars, checkContext e vars [x] b]
  let two = [[x, y] | x <- vars, y <- vars, checkContext e vars [x, y] b]
  let three = [vars | checkContext e vars vars b]
  let all = zero ++ one ++ two ++ three
  if L.null all
    then Nothing
    else Just (L.head all)

baseProof :: [Expr] -> Expr -> (Bool, [Expr])
baseProof context e@(Var s) =
  if e `L.elem` context
    then (True, [e])
    else (False, [Not e])
baseProof c (Not e) =
  case baseProof c e of
    (False, p) -> (True, p)
    (True, p) -> (False, p ++ helper)
      where helper =
              substitution
                (Map.singleton "A" e)
                [ "A"
                , "A -> !A -> A"
                , "!A -> A"
                , "!A -> (!A -> !A) -> !A"
                , "!A -> !A -> !A"
                , "(!A -> (!A -> !A)) -> (!A -> (!A -> !A) -> !A) -> (!A -> !A)"
                , "(!A -> (!A -> !A) -> !A) -> (!A -> !A)"
                , "!A -> !A"
                , "(!A -> A) -> (!A -> !A) -> !!A"
                , "(!A -> !A) -> !!A"
                , "!!A"
                ]
baseProof c (Op op l r) = (opF, prl ++ prr ++ opP)
  where
    (f, prl) = baseProof c l
    (f1, prr) = baseProof c r
    sub = substitution (Map.fromList [("A", l), ("B", r)])
    (opF, opP) = opProof sub op f f1

mergeProofs :: Expr -> Expr -> [Expr]
mergeProofs var d = do
  let sub = substitution (Map.fromList [("A", var), ("D", d)])
  let f = head $ sub ["A -> D"]
  let (_, ff) = aImplB f
  let f1 = head $ sub ["!A -> D"]
  let (_, ff1) = aImplB f1
  let t = sub ["(!D -> !A) -> (!D -> !!A) -> !!D", "(!D -> !!A) -> !!D", "!!D", "!!D -> D", "D"]
  ff ++ ff1 ++ t

proof :: [String] -> [Expr] -> Expr -> [Expr]
proof vars context d =
  if L.null vars
    then let (_, p) = baseProof context d
          in p
    else do
      let fstContext = context ++ [Var $ head vars]
      let sndContext = context ++ [Not $ Var $ head vars]
      let fstProof = proof (tail vars) fstContext d
      let sndProof = proof (tail vars) sndContext d
      let (_, dedFstProof) = deduction (fstContext, fstProof)
      let (_, dedSndProof) = deduction (sndContext, sndProof)
      let merge = mergeProofs (Var $ head vars) d
      dedFstProof ++ dedSndProof ++ merge

main :: IO ()
main
 = do
  i <- getLine
  let d = parser (alexScanTokens i)
  let t = getContext d True
  let f = getContext d False
  if isNothing t && isNothing f
    then putStrLn ":("
    else if isNothing t
           then do
             let context = fromJust f
             let vars = L.filter (`L.notElem` context) $ getVars d
             let newD = Not d
             let realContext = L.map (Not . Var) context
             let p = proof vars realContext newD
             --let m = annotateProof realContext p
             --if isNothing m
               --then error "Genereted proof is incorrect"
               --else do
             putStrLn $ contextToOut realContext newD
             mapM_ print p
           else do
             let context = fromJust t
             let vars = L.filter (`L.notElem` context) $ getVars d
             let realContext = L.map Var context
             let p = proof vars realContext d
             --let m = annotateProof realContext p
             --if isNothing m
               --then error "Genereted proof is incorrect"
               --else do
             putStrLn $ contextToOut realContext d
             mapM_ print p
