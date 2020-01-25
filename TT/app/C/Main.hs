module Main where

import           Control.Exception
import           Data.List
import qualified Data.Map          as Map
import           Data.Maybe        (fromJust)
import qualified Data.Set          as Set
import           Lexer             (alexScanTokens)
import           Parser            (parser)
import           Types

main :: IO ()
main = do
  input <- getLine
  e <- try (evaluate (parser (alexScanTokens input))) :: IO (Either SomeException Expr)
  case e of
    Left err -> error ":("
    Right expr -> do
      let (set, t, context) = prepare expr
      case unification set of
        Nothing -> putStrLn "Expression has no type"
        Just eq -> do
            let st = Map.fromList $ map mp eq
            printProof (Map.fromList (map (\(k, v) -> (k, substitution v st)) (Map.toList context))) st t 0

prepare :: Expr -> ([Eqq], TypedExpr, Map.Map String Type)
prepare e = fst $ helper e 0 Map.empty
  where
    helper :: Expr -> Int -> Map.Map String Int -> (([Eqq], TypedExpr, Map.Map String Type), (Map.Map String Int, Int))
    helper (Abstraction name e) n map = ((s, TAbstraction name te (Impl (AtomVar n) (getType te)), Map.delete name mp), (nmap, n1))
      where
        ((s, te, mp), (nmap, n1)) = helper e (n + 1) (Map.insert name n map)
    helper (Application e1 e2) n map =
      ( (Eqq (getType te1) (Impl (getType te2) (AtomVar n2)) : s1 ++ s2, TApplication te1 te2 $ AtomVar n2, Map.union mp1 mp2)
      , (nmap2, n2 + 1))
      where
        ((s1, te1, mp1), (nmap1, n1)) = helper e1 n map
        ((s2, te2, mp2), (nmap2, n2)) = helper e2 n1 nmap1
    helper (Var name) n map =
      if Map.member name map
        then let tp = AtomVar $ map Map.! name
              in (([], TVar name tp, Map.singleton name tp), (map, n))
        else let tp = AtomVar n
              in (([], TVar name tp, Map.singleton name tp), (Map.insert name n map, n + 1))

unification :: [Eqq] -> Maybe [Eqq]
unification eq
  | check eq = Nothing
  | sort nxt == sort eq = Just eq
  | otherwise = unification nxt
  where
    nxt = sub Set.empty $ reduc eq
    reduc :: [Eqq] -> [Eqq]
    reduc [] = []
    reduc (h:hs) =
      case h of
        (Eqq e@(Impl _ _) v@(AtomVar _)) -> Eqq v e : reduc hs
        (Eqq v1@(AtomVar i) v2@(AtomVar j)) ->
          if i == j
            then reduc hs
            else h : reduc hs
        (Eqq (Impl e1 e2) (Impl e3 e4)) -> reduc $ Eqq e1 e3 : Eqq e2 e4 : hs
        _ -> h : reduc hs
    check :: [Eqq] -> Bool
    check [] = False
    check (h:hs) =
      case h of
        (Eqq (AtomVar i) (Impl e1 e2)) -> (helper e1 i || helper e2 i) || check hs
        _ -> check hs
      where
        helper :: Type -> Int -> Bool
        helper (Impl e1 e2) i = helper e1 i || helper e2 i
        helper (AtomVar i) j  = j == i
    sub :: Set.Set Int -> [Eqq] -> [Eqq]
    sub set eqq
     =
      case aa of
        Nothing -> eqq
        Just f@(Eqq (AtomVar i) _) -> sub (Set.insert i set) (f : helper (delete f eqq) (uncurry Map.singleton $ mp f))
      where
        aa = find (filt set) eqq
        helper :: [Eqq] -> Map.Map Int Type -> [Eqq]
        helper [] _ = []
        helper (Eqq t1 t2:hs) map = Eqq (substitution t1 map) (substitution t2 map) : helper hs map

filt set (Eqq (AtomVar i) _)
  | not $ Set.member i set = True
filt _ _ = False

mp (Eqq (AtomVar i) t) = (i, t)
mp e = error $ show e

getType :: TypedExpr -> Type
getType (TAbstraction _ _ tp) = tp
getType (TApplication _ _ tp) = tp 
getType (TVar _ tp) = tp

substitution :: Type -> Map.Map Int Type -> Type
substitution (Impl e1 e2) map = Impl (substitution e1 map) (substitution e2 map)
substitution me@(AtomVar i) map =
  if Map.member i map
    then map Map.! i
    else me

printProof :: Map.Map String Type -> Map.Map Int Type -> TypedExpr -> Int -> IO()
printProof cont sb me@(TAbstraction name e myType@(Impl l r)) lvl = do
  printContext cont lvl
  putStr $ show me 
  putStr " : "
  putStr $ show $ substitution myType sb
  putStrLn " [rule #3]"
  printProof (Map.insert name (substitution l sb) cont) sb e (lvl + 1)
printProof cont sb me@(TApplication e1 e2 myType) lvl = do
  printContext cont lvl
  putStr $ show me 
  putStr " : "
  putStr $ show $ substitution myType sb
  putStrLn " [rule #2]"
  printProof cont sb e1 (lvl + 1)
  printProof cont sb e2 (lvl + 1)
printProof cont sb me@(TVar name myType) lvl = do
  printContext cont lvl
  putStr $ show me 
  putStr " : "
  putStr $ show $ substitution myType sb
  putStrLn " [rule #1]"
 
 
printContext :: Map.Map String Type -> Int -> IO()
printContext cont lvl = do 
  putStr $ take (4 * lvl) $ cycle "*   "
  putStr $ intercalate ", " $ map (\(k, v) -> k ++ " : " ++ show v) $ Map.toList cont
  if null cont 
    then putStr "|-"
    else putStr " |- "
