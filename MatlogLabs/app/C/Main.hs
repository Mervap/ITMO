module Main where

import           Data.List       as L
import qualified Data.Map.Strict as Map
import           Data.Maybe
import           Lexer           (alexScanTokens)
import           Parser          (parser)
import           ParserContext   (parserC)
import           System.IO
import           Types
import           Utils

commonAxConverter :: Expr -> [Expr]
commonAxConverter ax =
  substitution
    (Map.singleton "AX" ax)
    [ "AX"
    , "AX -> !AX -> AX"
    , "!AX -> AX"
    , "!AX -> !AX -> !AX"
    , "!AX -> (!AX -> !AX) -> !AX"
    , "(!AX -> !AX -> !AX) -> (!AX -> (!AX -> !AX) -> !AX) -> (!AX -> !AX)"
    , "(!AX -> (!AX -> !AX) -> !AX) -> (!AX -> !AX)"
    , "!AX -> !AX"
    , "(!AX -> AX) -> (!AX -> !AX) -> !!AX"
    , "(!AX -> !AX) -> !!AX"
    , "!!AX"
    ]

ax10Converter :: Expr -> [Expr]
ax10Converter (Op Impl (Not (Not a)) b) = do
  let sub = substitution (Map.singleton "A" a)
  let f = head $ sub ["A -> !!A -> A"]
  let (_, f1) = aImplB f -- !(!!A -> A) -> !A
  let ff = head $ sub ["!A -> !!A -> A"]
  let (_, ff1) = aImplB ff -- !(!!A -> A) -> !!A
  f :
    f1 ++
    ff :
    ff1 ++
    sub
      [ "(!(!!A -> A) -> !A) -> (!(!!A -> A) -> !!A) -> !!(!!A -> A)"
      , "(!(!!A -> A) -> !!A)->!!(!!A -> A)"
      , "!!(!!A -> A)"
      ]

axConverter :: AnnotatedExpr -> [Expr]
axConverter (Ax 10 expr) = ax10Converter expr
axConverter (Ax _ expr)  = commonAxConverter expr
axConverter er           = error (show er ++ " is not a Ax")

hypConverter :: AnnotatedExpr -> [Expr]
hypConverter (Hyp _ expr) = commonAxConverter expr
hypConverter er           = error (show er ++ " is not a Hyp")

mpHelper1 :: ([String] -> [Expr]) -> [Expr]
mpHelper1 sub = do
  let f = head $ sub ["!!A"]
  let f1 = head $ sub ["A -> B"]
  let (f2, ff) = aImplB f1
  let (_, ff1) = aImplB f2
  let f4 = sub ["!!B"]
  f : f1 : ff ++ ff1 ++ f4

mpHelper2 :: ([String] -> [Expr]) -> [Expr]
mpHelper2 sub =
  sub
    [ "!B"
    , "!B -> (A -> B) -> !B"
    , "(A -> B) -> !B"
    , "((A -> B) -> !B) -> ((A -> B) -> !!B) -> !(A -> B)"
    , "((A -> B) -> !!B) -> !(A -> B)"
    , "!(A -> B)"
    ]

mpHelper3 :: ([String] -> [Expr]) -> [Expr]
mpHelper3 sub =
  sub
    [ "!!(A -> B)"
    , "!!(A -> B) -> !B -> !!(A -> B)"
    , "!B -> !!(A -> B)"
    , "(!B -> !(A -> B)) -> (!B -> !!(A -> B)) -> !!B"
    , "(!B -> !!(A -> B)) -> !!B"
    , "!!B"
    ]

mpConverter :: Map.Map Int Expr -> AnnotatedExpr -> [Expr]
mpConverter map (MP _ n expr) = do
  let (Just a) = Map.lookup n map
  let sub = substitution (Map.fromList [("A", a), ("B", expr)])
  let context1 = sub ["!!A", "!!(A -> B)", "!B", "A -> B"]
  let proof1 = mpHelper1 sub
  let (context2, proof2) = deductionIntuit (context1, proof1) -- (A -> B) -> !!B
  let proof3 = proof2 ++ mpHelper2 sub
  let (context3, proof4) = deductionIntuit (context2, proof3) -- !B -> !(A -> B)
  proof4 ++ mpHelper3 sub

mpConverter _ er = error (show er ++ " is not a MP")

converter :: Map.Map Int Expr -> AnnotatedExpr -> [Expr]
converter map e@(Ax _ _)   = axConverter e
converter map e@(Hyp _ _)  = hypConverter e
converter map e@(MP _ _ _) = mpConverter map e

main :: IO ()
main
  --x <- openFile "D:/ITMO/MatlogLabs/app/FirstB/1" ReadMode
  --i <- hGetLine x
 = do
  i <- getLine
  let (context, d) = parserC (alexScanTokens i)
  input <- getContents
  let proof = map (parser . alexScanTokens) (lines input)
  let anProof = map snd $ annotateCorrectProof False context proof
  let newProof = concatMap (converter (Map.fromList (zip [1 ..] proof))) anProof
  putStrLn $ contextToOut context (Not $ Not d)
  mapM_ print newProof
