-- This is terrible and unreadable, sorry
module Utils where

import           Control.Exception
import           Control.Monad.State.Lazy
import qualified Data.Map                 as Map
import qualified Data.Set                 as Set
import           Types

alphaReduction :: Expr -> String -> String -> State (Map.Map Int Expr) Expr
alphaReduction me@(Abstraction name e) old new = do
  ne <- alphaReduction e old new
  if name == old
    then return me
    else return $ Abstraction name ne
alphaReduction (Application e1 e2) old new = do
  ne1 <- alphaReduction e1 old new
  ne2 <- alphaReduction e2 old new
  return $ Application ne1 ne2
alphaReduction me@(Var name) old new =
  if name == old
    then return $ Var new
    else return me
alphaReduction me@(Wrap n) old new = do
  map <- get
  ne <- alphaReduction (map Map.! n) old new
  modify $ Map.insert n ne
  return me

isNormal :: Expr -> State (Map.Map Int Expr) Bool
isNormal (Application (Abstraction name to) from) = return False
isNormal (Application e1@(Wrap i) e2) = do
  map <- get
  let ne = map Map.! i
  case ne of
    Abstraction _ _ -> return False
    Wrap i -> do
      map <- get
      isNormal (Application (map Map.! i) e2)
    e -> do
      normal <- isNormal e
      if normal
        then isNormal e2
        else return False
isNormal (Application e1 e2) = do
  normal1 <- isNormal e1
  if normal1
    then isNormal e2
    else return False
isNormal (Abstraction _ e) = isNormal e
isNormal me@(Wrap i) = do
  map <- get
  isNormal (map Map.! i)
isNormal e = return True

memorizedBetaReduction :: Expr -> Int -> State (Map.Map Int Expr) Triple
memorizedBetaReduction e mx = do
  (normal, Pair red mxN) <- findSubstitute e mx
  return $ Triple red normal mxN
  where
    findSubstitute :: Expr -> Int -> State (Map.Map Int Expr) (Bool, Pair)
    findSubstitute (Application (Abstraction name to) from) max = do
      let wr = Wrap max
      modify $ Map.insert max from
      fr1 <- getFree from
      fr2 <- getFree to
      e <- substitute to name wr (Set.union fr1 fr2)
      return (False, Pair e $ max + 1)
    findSubstitute me@(Application e1@(Wrap i) e2) max = do
      map <- get
      ne <- helper (map Map.! i)
      case ne of
        Abstraction _ _ -> findSubstitute (Application ne e2) max
        _ -> do
          let ne = map Map.! i
          (fstNormal, Pair ne1 mx1) <- findSubstitute ne max
          if not fstNormal
            then do
              modify $ Map.insert i ne1
              return (fstNormal, Pair me mx1)
            else do
              (sndNormal, Pair ne2 mx2) <- findSubstitute e2 mx1
              return (sndNormal, Pair (Application e1 ne2) mx2)
      where 
        helper :: Expr ->  State (Map.Map Int Expr) Expr
        helper (Wrap i) = do
          map <- get 
          helper (map Map.! i)
        helper e = return e
    findSubstitute (Application e1 e2) max = do
      (fstNormal, Pair ne1 mx1) <- findSubstitute e1 max
      if not fstNormal
        then return (fstNormal, Pair (Application ne1 e2) mx1)
        else do
          (sndNormal, Pair ne2 mx2) <- findSubstitute e2 mx1
          return (sndNormal, Pair (Application e1 ne2) mx2)
    findSubstitute (Abstraction name e) max = do
      (normal, Pair ne mx) <- findSubstitute e max
      return (normal, Pair (Abstraction name ne) mx)
    findSubstitute me@(Wrap i) max = do
      map <- get
      (normal, Pair ne mx) <- findSubstitute (map Map.! i) max
      modify $ Map.insert i ne
      return (normal, Pair me mx)
    findSubstitute e max = return (True, Pair e max)

substitute :: Expr -> String -> Expr -> Set.Set String -> State (Map.Map Int Expr) Expr
substitute me@(Abstraction myName e) name from fr
  | myName == name || name == newName = return me
  | Set.member myName fr = do
    alf <- alphaReduction e myName newName
    ne <- substitute alf name from fr
    return $ Abstraction newName ne
  | otherwise = do
    ne <- substitute e name from fr
    return $ Abstraction myName ne
  where
    newName = genName myName 0
    genName :: String -> Int -> String
    genName old i
      | not (Set.member new fr) = new
      | otherwise = genName new (i + 1)
      where
        new = old ++ show i
substitute (Application e1 e2) name from fr = do
  ne1 <- substitute e1 name from fr
  ne2 <- substitute e2 name from fr
  return $ Application ne1 ne2
substitute me@(Var my) name from _ =
  if my == name
    then return from
    else return me
substitute me@(Wrap i) name from fr = do
  map <- get
  let ne = map Map.! i
  case ne of
    Abstraction myName _
      | name == myName -> return me
    e -> substitute e name from fr

getFree :: Expr -> State (Map.Map Int Expr) (Set.Set String)
getFree (Abstraction name e) = do
  fr <- getFree e
  return $ Set.delete name fr
getFree (Application e1 e2) = do
  fr1 <- getFree e1
  fr2 <- getFree e2
  return $ Set.union fr1 fr2
getFree (Var name) = return $ Set.singleton name
getFree (Wrap i) = do
  map <- get
  getFree (map Map.! i)

showE :: Expr -> Map.Map Int Expr -> String
showE (Abstraction var expr) mp = "(\\" ++ var ++ "." ++ showE expr mp ++ ")"
showE (Application e1 e2) mp = "(" ++ showE e1 mp ++ " " ++ showE e2 mp ++ ")"
showE (Var name) _ = name
--showE (Wrap i) mp = showE (mp Map.! i) mp
showE (Wrap i) mp = "W#" ++ show i ++ "#(" ++ showE (mp Map.! i) mp ++ ")"
