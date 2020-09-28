{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE TypeFamilies               #-}

module Task4.HalyavaScriptPrinter
  ( -- * The type of js-like scripts converting
    JSToStr(..)
    
    -- * Function for converting js-like scripts.
  , jsToString
  , jsToString1
  , jsToString2
  ) where

import Control.Monad.State
import Control.Monad.Writer.Strict

import Task3.HalyavaScript

type Converter = WriterT String (State (Int, Int))

-- | The type of js-like scripts converting
newtype JSToStr s a =
  JSToStr
    { convert :: Converter a
    }
  deriving (Functor, Applicative, Monad)

type instance Var (JSToStr s) a = String

-- | Add indentation to the output
tellInd :: Converter ()
tellInd = do
  ind <- gets fst
  tell $ replicate ind ' '

withoutIndVoid :: Converter a -> Converter ()
withoutIndVoid = void . withoutInd

-- | Convert piece of code without indentation
withoutInd :: Converter a -> Converter a
withoutInd action = do
  ind <- gets fst
  putInd 0
  res <- action
  putInd ind
  return res
  where
    putInd :: Int -> WriterT String (State (Int, Int)) ()
    putInd p = modify (\(_, n) -> (p, n))

withIncIndVoid :: Converter a -> Converter ()
withIncIndVoid = void . withIncInd

-- | Convert piece of code with increased indentation
withIncInd :: Converter a -> Converter a
withIncInd action = do
  tell " {"
  modify (\(p, n) -> (p + 2, n))
  res <- action
  modify (\(p, n) -> (p - 2, n))
  tell "\n"
  tellInd
  tell "}\n"
  return res

-- | Convert operator expression
operatorToString :: String -> JSToStr s Val -> JSToStr s Val -> Converter Val
operatorToString op a b = do
  tellInd
  tell "("
  withoutIndVoid $ do
    void $ convert a
    tell $ " " ++ op ++ " "
    void $ convert b
  tell ")"
  return NaN

-- | Return next unoccupied 'Var' name
getNextName :: Converter String
getNextName = do
  cur <- gets snd
  modify ((+ 1) <$>)
  return $ "v" ++ show cur

instance JSScript (JSToStr s) where
  num n      = JSToStr $ tell (show n) >> return NaN
  str s      = JSToStr $ tell (show s) >> return NaN
  bool True  = JSToStr $ tell "true"   >> return NaN
  bool False = JSToStr $ tell "false"  >> return NaN
  nan        = JSToStr $ tell "NaN"    >> return NaN
  ref @<- val =
    JSToStr $ do
      tell "\n"
      tellInd
      tell ref
      tell " = "
      withoutIndVoid $ convert val
  a %+% b = JSToStr $ operatorToString "+" a b
  a %*% b = JSToStr $ operatorToString "*" a b
  a %-% b = JSToStr $ operatorToString "-" a b
  a %/% b = JSToStr $ operatorToString "/" a b
  a %<% b = JSToStr $ operatorToString "<" a b
  a @+% b = JSToStr $ operatorToString "+" (readVar a) b
  a @*% b = JSToStr $ operatorToString "*" (readVar a) b
  a @-% b = JSToStr $ operatorToString "-" (readVar a) b
  a @/% b = JSToStr $ operatorToString "/" (readVar a) b
  a @<% b = JSToStr $ operatorToString "<" (readVar a) b
  a %+@ b = JSToStr $ operatorToString "+" a (readVar b)
  a %*@ b = JSToStr $ operatorToString "*" a (readVar b)
  a %-@ b = JSToStr $ operatorToString "-" a (readVar b)
  a %/@ b = JSToStr $ operatorToString "/" a (readVar b)
  a %<@ b = JSToStr $ operatorToString "<" a (readVar b)
  a @+@ b = JSToStr $ operatorToString "+" (readVar a) (readVar b)
  a @*@ b = JSToStr $ operatorToString "*" (readVar a) (readVar b)
  a @-@ b = JSToStr $ operatorToString "-" (readVar a) (readVar b)
  a @/@ b = JSToStr $ operatorToString "/" (readVar a) (readVar b)
  a @<@ b = JSToStr $ operatorToString "<" (readVar a) (readVar b)
  sAbs v =
    JSToStr $ do
      tellInd
      tell "Math.abs("
      s <- withoutInd $ convert v
      tell ")"
      return s
  readVar ref = JSToStr $ tellInd >> tell ref >> return NaN
  new val =
    JSToStr $ do
      name <- getNextName
      tell "\n"
      tellInd
      tell "var "
      tell name
      tell " = "
      withoutIndVoid $ convert val
      return name
  sFun0 f =
    JSToStr $ do
      tell "function()"
      withIncIndVoid $ convert f
      return NaN
  sFun1 f _ =
    JSToStr $ do
      name <- getNextName
      tell "function(" >> tell name >> tell ")"
      withIncIndVoid $ convert (f name)
      return NaN
  sFun2 f _ _ =
    JSToStr $ do
      name1 <- getNextName
      name2 <- getNextName
      tell "function(" >> tell name1 >> tell ", " >> tell name2 >> tell ")"
      withIncIndVoid $ convert (f name1 name2)
      return NaN
  sIf cont sThen sElse =
    JSToStr $ do
      tell "\n"
      tellInd
      tell "if ("
      withoutIndVoid $ convert cont
      tell ")"
      res <- withIncInd $ convert sThen
      tellInd
      tell "else"
      withIncIndVoid $ convert sElse
      return res
  sWhile cond body =
    JSToStr $ do
      tell "\n"
      tellInd
      tell "while ("
      withoutIndVoid $ convert cond
      tell ")"
      withIncIndVoid $ convert body
  ret v =
    JSToStr $ do
      tellInd
      tell "return "
      withoutInd $ convert v
  retVar ref =
    JSToStr $ do
      tellInd
      tell "return "
      withoutInd $ convert (readVar ref)
      
-- | Convert js-like function without arguments to the string
jsToString :: JSToStr s a -> String
jsToString script = evalState (execWriterT $ convert script) (0, 0)

-- | Convert js-like function with single argument to the string
jsToString1 :: (JSToStr s Val -> JSToStr s a) -> String
jsToString1 script = jsToString (script nan)

-- | Convert js-like function with two arguments to the string
jsToString2 :: (JSToStr s Val -> JSToStr s Val -> JSToStr s a) -> String
jsToString2 script = jsToString (script nan nan)
