{-# LANGUAGE RankNTypes            #-}
{-# LANGUAGE TypeFamilies          #-}

module Task3.HalyavaScript
  ( -- * The types of js-like primitives.
    Var
  , Val(..)
  , JSNum(..)
  
    -- * The @JSScript@ class type.
  , JSScript(..)

    -- * Function for run js-like scripts.
  , interpretJS
  , interpretJS1
  , interpretJS2

    -- * Some examples written on js-like syntax.
  , sqrt2
  , log2
  , strReplicate
  ) where

import Control.Applicative (liftA2)
import Control.Monad (when)
import Control.Monad.ST.Strict
import Data.STRef

infixl 3 @<-
infix  4 %<%, @<%, %<@, @<@
infixl 6 %+%, @+%, %+@, @+@
infixl 6 %-%, @-%, %-@, @-@
infixl 7 %*%, @*%, %*@, @*@
infixl 7 %/%, @/%, %/@, @/@

-- | JS-like numbers
data JSNum
  = Int32 Int
  | Frac Double

instance Show JSNum where
  show (Int32 a) = show a
  show (Frac a)  = show a

instance Num JSNum where
  (Int32 a) + (Int32 b) = Int32 $ a + b
  (Int32 a) + (Frac b)  = Frac  $ fromIntegral a + b
  (Frac a)  + (Int32 b) = Frac  $ a + fromIntegral b
  (Frac a)  + (Frac b)  = Frac  $ a + b
  (Int32 a) - (Int32 b) = Int32 $ a - b
  (Int32 a) - (Frac b)  = Frac  $ fromIntegral a - b
  (Frac a)  - (Int32 b) = Frac  $ a - fromIntegral b
  (Frac a)  - (Frac b)  = Frac  $ a - b
  (Int32 a) * (Int32 b) = Int32 $ a * b
  (Int32 a) * (Frac b)  = Frac  $ fromIntegral a * b
  (Frac a)  * (Int32 b) = Frac  $ a * fromIntegral b
  (Frac a)  * (Frac b)  = Frac  $ a * b
  abs (Int32 a)         = Int32 $ abs a
  abs (Frac a)          = Frac  $ abs a
  signum (Int32 a)      = Int32 $ signum a
  signum (Frac a)       = Frac  $ signum a
  fromInteger a         = Int32 $ fromInteger a

instance Fractional JSNum where
  fromRational a        = Frac $ fromRational a
  (Int32 a) / (Int32 b) = Frac $ fromIntegral a / fromIntegral b
  (Int32 a) / (Frac b)  = Frac $ fromIntegral a / b
  (Frac a)  / (Int32 b) = Frac $ a / fromIntegral b
  (Frac a)  / (Frac b)  = Frac $ a / b

instance Eq JSNum where
  Int32 x == Int32 y = x == y
  Int32 x == Frac y  = fromIntegral x == y
  Frac x  == Int32 y = x == fromIntegral y
  Frac x  == Frac y  = x == y

instance Ord JSNum where
  Int32 x <= Int32 y = x <= y
  Int32 x <= Frac y  = fromIntegral x <= y
  Frac x  <= Int32 y = x <= fromIntegral y
  Frac x  <= Frac y  = x <= y

-- | JS-like values
data Val
  = Number JSNum  -- ^ Numbers
  | Boolean Bool  -- ^ Boolean values
  | Str String    -- ^ String values
  | NaN           -- ^ Not-a-Number
  deriving (Eq)

boolToJsNumber :: Bool -> Val
boolToJsNumber b = Number $ Int32 (fromEnum b)

jsValToBool :: Val -> Bool
jsValToBool (Number a)  = a /= 0
jsValToBool (Boolean b) = b
jsValToBool (Str s)     = null s
jsValToBool NaN         = False

instance Show Val where
  show (Number a)      = show a
  show (Str s)         = s
  show (Boolean True)  = "true"
  show (Boolean False) = "false"
  show NaN             = "NaN"

-- | Type of variable in js-like syntax
type family Var (js :: * -> *) a :: *

-- | Sum of two js-like values. Different marginal behavior based on JS
-- is taken into account, but of course not all
jsSum :: Val -> Val -> Val
jsSum NaN _                 = NaN
jsSum _ NaN                 = NaN
jsSum (Number a) (Number b) = Number $ a + b
jsSum (Str a) b             = Str $ a ++ show b
jsSum a (Str b)             = Str $ show a ++ b
jsSum (Boolean a) b         = jsSum (boolToJsNumber a) b
jsSum a (Boolean b)         = jsSum a (boolToJsNumber b)

-- | Subtraction of two js-like values. Different marginal behavior based on JS
-- is taken into account, but of course not all
jsSub :: Val -> Val -> Val
jsSub NaN _                 = NaN
jsSub _ NaN                 = NaN
jsSub (Number a) (Number b) = Number $ a - b
jsSub (Str "") b            = jsSub (Number 0) b
jsSub (Str _) _             = NaN
jsSub a (Str "")            = jsSub a (Number 0)
jsSub _ (Str _)             = NaN
jsSub (Boolean a) b         = jsSub (boolToJsNumber a) b
jsSub a (Boolean b)         = jsSub a (boolToJsNumber b)

-- | Multiplication of two js-like values. Different marginal behavior based on JS
-- is taken into account, but of course not all
jsMul :: Val -> Val -> Val
jsMul NaN _                 = NaN
jsMul _ NaN                 = NaN
jsMul (Number a) (Number b) = Number $ a * b
jsMul (Str "") b            = jsMul (Number 0) b
jsMul (Str _) _             = NaN
jsMul a (Str "")            = jsMul a (Number 0)
jsMul _ (Str _)             = NaN
jsMul (Boolean a) b         = jsMul (boolToJsNumber a) b
jsMul a (Boolean b)         = jsMul a (boolToJsNumber b)

-- | Division of two js-like values. Different marginal behavior based on JS
-- is taken into account, but of course not all
jsDiv :: Val -> Val -> Val
jsDiv NaN _                 = NaN
jsDiv _ NaN                 = NaN
jsDiv (Number 0) (Number 0) = NaN
jsDiv (Number a) (Number b) = Number $ a / b
jsDiv (Str "") b            = jsDiv (Number 0) b
jsDiv (Str _) _             = NaN
jsDiv a (Str "")            = jsDiv a (Number 0)
jsDiv _ (Str _)             = NaN
jsDiv (Boolean a) b         = jsDiv (boolToJsNumber a) b
jsDiv a (Boolean b)         = jsDiv a (boolToJsNumber b)

-- | Compares two js-like values on the subject of the relationship less.
-- Different marginal behavior based on JS is taken into account, but of course not all
jsLe :: Val -> Val -> Val
jsLe NaN _                 = Boolean False
jsLe _ NaN                 = Boolean False
jsLe (Number a) (Number b) = Boolean $ a < b
jsLe (Str a) (Str b)       = Boolean $ a < b
jsLe (Str _) _             = Boolean False
jsLe _ (Str _)             = Boolean False
jsLe (Boolean a) b         = jsLe (boolToJsNumber a) b
jsLe a (Boolean b)         = jsLe a (boolToJsNumber b)

-- | Absolute value of 'Val'
jsAbs :: Val -> Val
jsAbs (Number a)  = Number $ abs a
jsAbs (Str _)     = NaN
jsAbs (Boolean b) = boolToJsNumber b
jsAbs NaN         = NaN

-- | Class, describing js-like syntax. In operators @\@@ means "'Var' on this side",
-- @%@ - "'Val' on this side"
class Monad js =>
      JSScript js
  where
  -- | Operation for creation a 'Val' from 'JSNum' in current script.
  --
  -- @
  -- num 10
  -- num 12.5
  -- @
  num   :: JSNum -> js Val

  -- | Operation for creation a 'Val' from 'String' in current script.
  --
  -- @srt "Hello"@
  str   :: String -> js Val

  -- | Operation for creation a 'Val' from 'Bool' in current script.
  --
  -- @bool True@
  bool  :: Bool -> js Val

  -- | Operation for creation a 'NaN' 'Val' in current script.
  nan   :: js Val

  -- | Operation for rewriting an existing 'Var'.
  --
  -- @
  -- (var :: Var js Val) \@<- num 10
  -- (var :: Var js Val) \@<- nan
  -- @
  (@<-) :: Var js Val -> js Val -> js ()

  -- | Operations with two 'Val'
  --
  -- @
  -- num 10 %+% num 20
  -- @
  (%+%), (%-%), (%*%), (%/%), (%<%) :: js Val -> js Val -> js Val

  -- | Operations with 'Var' on left side and 'Val' on right side
  --
  -- @
  -- (var :: Var js Val) \@+% num 20
  -- @
  (@+%), (@-%), (@*%), (@/%), (@<%) :: Var js Val -> js Val -> js Val

  -- | Operations with 'Val' on left side and 'Var' on right side
  --
  -- @
  -- num 10 %+\@ (var :: Var js Val)
  -- @
  (%+@), (%-@), (%*@), (%/@), (%<@) :: js Val -> Var js Val -> js Val

  -- | Operations with two 'Var'
  --
  -- @
  -- (var1 :: Var js Val) \@+\@ (var2 :: Var js Val)
  -- @
  (@+@), (@-@), (@*@), (@/@), (@<@) :: Var js Val -> Var js Val -> js Val

  -- | Abs value in js-like syntax
  sAbs    :: js Val -> js Val

  -- | Reads 'Val' from 'Var'
  readVar :: Var js Val -> js Val

  -- | Operation for creating a new 'Var'. Using power of 'Monad's! Use @do@ like a brackets
  -- The creation syntax becomes very similar to the assignment syntax ('@<-')
  --
  -- @
  -- do
  --   var <- new (num 10)
  --   var \@<- num 20
  -- @
  new     :: js Val -> js (Var js Val)
  
  -- | If selection operator
  sIf     :: js Val -> js a -> js a -> js a
  
  -- | Creates a function without arguments
  sFun0   :: js Val -> js Val
  
  -- | Creates a function with single argument
  sFun1   :: (Var js Val -> js Val) -> js Val -> js Val
  
  -- | Creates a function with two arguments
  sFun2   :: (Var js Val -> Var js Val -> js Val) -> js Val -> js Val -> js Val
  
  -- | While loop
  sWhile  :: js Val -> js () -> js ()
  
  -- | Returns value from the function
  ret     :: js a -> js a
  
  -- | Returns value from 'Var' from the function
  retVar  :: Var js Val -> js Val

type instance Var (ST s) a = STRef s a

instance JSScript (ST s) where
  num a = return $ Number a
  bool b = return $ Boolean b
  str s = return $ Str s
  nan = return NaN
  (%+%) = liftA2 jsSum
  (%*%) = liftA2 jsMul
  (%-%) = liftA2 jsSub
  (%/%) = liftA2 jsDiv
  (%<%) = liftA2 jsLe
  (@+%) a = (readVar a %+%)
  (@*%) a = (readVar a %*%)
  (@-%) a = (readVar a %-%)
  (@/%) a = (readVar a %/%)
  (@<%) a = (readVar a %<%)
  (%+@) a b = a %+% readVar b
  (%*@) a b = a %*% readVar b
  (%-@) a b = a %-% readVar b
  (%/@) a b = a %/% readVar b
  (%<@) a b = a %<% readVar b
  (@+@) a b = readVar a %+% readVar b
  (@*@) a b = readVar a %*% readVar b
  (@-@) a b = readVar a %-% readVar b
  (@/@) a b = readVar a %/% readVar b
  (@<@) a b = readVar a %<% readVar b
  ref @<- jsNewVal = do
    newVal <- jsNewVal
    writeSTRef ref newVal
  sAbs = (jsAbs <$>)
  readVar = readSTRef
  new val = val >>= newSTRef
  sFun0 fun = fun
  sFun1 fun val = new val >>= fun
  sFun2 fun val1 val2 = new val1 >>= \r1 -> new val2 >>= fun r1
  sIf cond sThen sElse = do
    flag <- cond
    if jsValToBool flag
      then sThen
      else sElse
  sWhile cond body = do
    flag <- cond
    when (jsValToBool flag) $ body >> sWhile cond body
  ret = id
  retVar = readVar

-- | Calculates the result of the function without arguments
interpretJS :: (forall s. ST s a) -> a
interpretJS = runST

-- | Calculates the result of the function with single argument
interpretJS1 :: Val -> (forall s. ST s Val -> ST s a) -> a
interpretJS1 x script = runST $ script (return x)

-- | Calculates the result of the function with two arguments
interpretJS2 :: Val -> Val -> (forall s. ST s Val -> ST s Val -> ST s a) -> a
interpretJS2 x y script = runST $ script (return x) (return y)

-- | For a given @x@ calculates @ceiling (log2 (x))@
log2 :: JSScript js => js Val -> js Val
log2 =
  sFun1 $ \a -> do
    acc <- new (num 1)
    logCnt <- new (num 0)
    sWhile (acc @<@ a) $ do
      acc @<- acc @+@ acc
      logCnt @<- logCnt @+% num 1
    retVar logCnt

-- | For a given @x@ calculates @sqrt (x)@
sqrt2 :: JSScript js => js Val -> js Val
sqrt2 =
  sFun1 $ \a -> do
    l <- new (num 0)
    r <- new (readVar a)
    sWhile (num 0.00001 %<% r @-@ l) $ do
      let m = (r @+@ l) %/% num 2
      sIf (m %*% m %<@ a) (l @<- m) (r @<- m)
    ret (str "Sqrt of " %+@ a %+% str " is " %+@ l)

-- | For a given @s@ and @cnt@ calculates @replicate cnt s@
strReplicate :: JSScript js => js Val -> js Val -> js Val
strReplicate =
  sFun2 $ \s cnt -> do
    ans <- new (str "")
    sWhile (num 0 %<@ cnt) $ do
      ans @<- ans @+@ s
      cnt @<- cnt @-% num 1
    retVar ans
