module Main where

import           Control.Exception
import           Control.Monad.State.Lazy
import           Data.List
import qualified Data.Map                 as Map
import qualified Data.Set                 as Set
import           Lexer                    (alexScanTokens)
import           Parser                   (parser)
import           Types
import           Utils

main :: IO ()
main = do
  input <- getLine
  let (m, k) = (\x -> (head x, head $ tail x)) (map read (words input) :: [Int])
  input <- getLine
  e <- try (evaluate (parser (alexScanTokens input))) :: IO (Either SomeException Expr)
  case e of
    Left err -> error ":("
    Right expr -> do
      let (res, map) = runState (repeatNTimes expr m k 0) Map.empty
      mapM_ putStrLn res

repeatKTimes :: Expr -> Int -> Int -> State (Map.Map Int Expr) Triple
repeatKTimes last k mx = do
  a <- get
  normal <- isNormal last
  if k == 0 || normal
    then return $ Triple last normal mx
    else do
      (Triple e _ mx1) <- memorizedBetaReduction last mx
      repeatKTimes e (k - 1) mx1

repeatNTimes :: Expr -> Int -> Int -> Int -> State (Map.Map Int Expr) [String]
repeatNTimes e n k mx = do
  normal <- isNormal e
  mapE <- get
  if n == 0 || normal
    then return [showE e mapE]
    else do
      let nextK = min n k
      (Triple next _ mx1) <- repeatKTimes e nextK mx
      mapNext <- get
      if n < k
        then if normal
               then return [showE e mapE, showE next mapNext]
               else return [showE e mapE]
        else if normal
               then return [showE e mapE, showE next mapNext]
               else do
                 tail <- repeatNTimes next (n - k) k mx1
                 return $ showE e mapE : tail
