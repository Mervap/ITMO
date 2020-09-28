{-# LANGUAGE ScopedTypeVariables #-}

module Task3
  ( task3TestGroup
  ) where

import Control.Monad.ST.Strict
import Hedgehog
import qualified Hedgehog.Gen as Gen
import qualified Hedgehog.Range as Range
import Test.Tasty
import Test.Tasty.Hedgehog
import Test.Tasty.HUnit

import Task3.HalyavaScript

genVal :: Gen Val
genVal = do
  n <- Gen.enum (-10000) 10000
  b <- Gen.bool
  s <- Gen.list (Range.linear 0 20) Gen.alpha
  Gen.element [Number (Int32 n), Boolean b, Str s, NaN]

testConst :: Assertion
testConst = do
  let n = 5 :: JSNum
  n @?= Int32 (5 :: Int)
  let d = 10.58 :: JSNum
  d @?= Frac (10.58 :: Double)
  let b = True
  let s = "Hello"
  interpretJS (num n)  @?= Number n
  interpretJS (num d)  @?= Number d
  interpretJS (bool b) @?= Boolean b
  interpretJS (str s)  @?= Str s
  interpretJS nan      @?= NaN

testVarValConversion :: Property
testVarValConversion =
  property $ do
    val <- forAll genVal
    interpretJS (new (return val) >>= \ref -> readVar ref) === val

testOpNanProp :: Property
testOpNanProp =
  property $ do
    val <- forAll genVal
    resR <-
      forAll $
      Gen.element
        [ interpretJS (return val %+% nan)
        , interpretJS (return val %-% nan)
        , interpretJS (return val %*% nan)
        , interpretJS (return val %/% nan)
        ]
    resL <-
      forAll $
      Gen.element
        [ interpretJS (nan %+% return val)
        , interpretJS (nan %-% return val)
        , interpretJS (nan %*% return val)
        , interpretJS (nan %/% return val)
        ]
    resL === NaN
    resR === NaN

testSum :: Assertion
testSum = do
  let n1 = num 10       :: ST s Val
  let n2 = num 25.5     :: ST s Val
  let s1 = str "Hello," :: ST s Val
  let s2 = str " world" :: ST s Val
  let f = bool False    :: ST s Val
  let t = bool True     :: ST s Val
  interpretJS (n1 %+% n2) @?= Number 35.5
  interpretJS (s1 %+% s2) @?= Str "Hello, world"
  interpretJS (f %+% f)   @?= Number 0
  interpretJS (f %+% t)   @?= Number 1
  interpretJS (t %+% t)   @?= Number 2
  interpretJS (n1 %+% s2) @?= Str "10 world"
  interpretJS (s1 %+% n2) @?= Str "Hello,25.5"
  interpretJS (f %+% s2)  @?= Str "false world"
  interpretJS (s1 %+% t)  @?= Str "Hello,true"

testSub :: Assertion
testSub = do
  let n2 = num 25.5    :: ST s Val
  let n1 = num 10      :: ST s Val
  let s0 = str ""      :: ST s Val
  let s1 = str "Hello" :: ST s Val
  let f = bool False   :: ST s Val
  let t = bool True    :: ST s Val
  interpretJS (n1 %-% n2) @?= Number (-15.5)
  interpretJS (n2 %-% n1) @?= Number 15.5
  interpretJS (s0 %-% s0) @?= Number 0
  interpretJS (s0 %-% s1) @?= NaN
  interpretJS (s1 %-% s0) @?= NaN
  interpretJS (f %-% f)   @?= Number 0
  interpretJS (f %-% t)   @?= Number (-1)
  interpretJS (t %-% f)   @?= Number 1
  interpretJS (t %-% t)   @?= Number 0
  interpretJS (n1 %-% s0) @?= Number 10
  interpretJS (s0 %-% n2) @?= Number (-25.5)
  interpretJS (s1 %-% n2) @?= NaN
  interpretJS (n1 %-% s1) @?= NaN
  interpretJS (n1 %-% f)  @?= Number 10
  interpretJS (n1 %-% t)  @?= Number 9
  interpretJS (f %-% n1)  @?= Number (-10)
  interpretJS (t %-% n1)  @?= Number (-9)
  interpretJS (f %-% s0)  @?= Number 0
  interpretJS (t %-% s0)  @?= Number 1
  interpretJS (s0 %-% f)  @?= Number 0
  interpretJS (s0 %-% t)  @?= Number (-1)

testMul :: Assertion
testMul = do
  let n1 = num 10      :: ST s Val
  let n2 = num 25.5    :: ST s Val
  let s1 = str "Hello" :: ST s Val
  let s0 = str ""      :: ST s Val
  let f = bool False   :: ST s Val
  let t = bool True    :: ST s Val
  interpretJS (n1 %*% n2) @?= Number 255.0
  interpretJS (s0 %*% s0) @?= Number 0
  interpretJS (s0 %*% s1) @?= NaN
  interpretJS (s1 %*% s0) @?= NaN
  interpretJS (f %*% f)   @?= Number 0
  interpretJS (f %*% t)   @?= Number 0
  interpretJS (t %*% t)   @?= Number 1
  interpretJS (n1 %*% s0) @?= Number 0
  interpretJS (s0 %*% n2) @?= Number 0
  interpretJS (s1 %*% n2) @?= NaN
  interpretJS (n1 %*% s1) @?= NaN
  interpretJS (n1 %*% f)  @?= Number 0
  interpretJS (n1 %*% t)  @?= Number 10
  interpretJS (f %*% s0)  @?= Number 0
  interpretJS (t %*% s0)  @?= Number 0
  interpretJS (s0 %*% f)  @?= Number 0
  interpretJS (s0 %*% t)  @?= Number 0

testDiv :: Assertion
testDiv = do
  let s0 = str ""      :: ST s Val
  let s1 = str "Hello" :: ST s Val
  let n1 = num 10      :: ST s Val
  let n2 = num 25.5    :: ST s Val
  let t = bool True    :: ST s Val
  let f = bool False   :: ST s Val
  let infty = Frac (1.0 / 0)
  interpretJS (num 5 %/% num 2) @?= Number 2.5
  interpretJS (n1 %/% n2) @?= Number (10 / 25.5)
  interpretJS (n2 %/% n1) @?= Number 2.55
  interpretJS (s0 %/% s0) @?= NaN
  interpretJS (s0 %/% s1) @?= NaN
  interpretJS (s1 %/% s0) @?= NaN
  interpretJS (f %/% f)   @?= NaN
  interpretJS (f %/% t)   @?= Number 0
  interpretJS (t %/% f)   @?= Number infty
  interpretJS (t %/% t)   @?= Number 1
  interpretJS (n1 %/% s0) @?= Number infty
  interpretJS (s0 %/% n2) @?= Number 0
  interpretJS (s1 %/% n2) @?= NaN
  interpretJS (n1 %/% s1) @?= NaN
  interpretJS (n1 %/% f)  @?= Number infty
  interpretJS (n1 %/% t)  @?= Number 10
  interpretJS (f %/% n1)  @?= Number 0
  interpretJS (t %/% n1)  @?= Number 0.1
  interpretJS (f %/% s0)  @?= NaN
  interpretJS (t %/% s0)  @?= Number infty
  interpretJS (s0 %/% f)  @?= NaN
  interpretJS (s0 %/% t)  @?= Number 0

testLe :: Assertion
testLe = do
  let n1 = num 10      :: ST s Val
  let n2 = num 25.5    :: ST s Val
  let s0 = str ""      :: ST s Val
  let s1 = str "Hello" :: ST s Val
  let s2 = str "Hellz" :: ST s Val
  let f = bool False   :: ST s Val
  let t = bool True    :: ST s Val
  interpretJS (n1 %<% n1) @?= Boolean False
  interpretJS (n1 %<% n2) @?= Boolean True
  interpretJS (n2 %<% n1) @?= Boolean False
  interpretJS (s0 %<% s0) @?= Boolean False
  interpretJS (s0 %<% s1) @?= Boolean True
  interpretJS (s1 %<% s0) @?= Boolean False
  interpretJS (s1 %<% s2) @?= Boolean True
  interpretJS (s2 %<% s1) @?= Boolean False
  interpretJS (f %<% f)   @?= Boolean False
  interpretJS (f %<% t)   @?= Boolean True
  interpretJS (t %<% f)   @?= Boolean False
  interpretJS (t %<% t)   @?= Boolean False
  interpretJS (n1 %<% s0) @?= Boolean False
  interpretJS (s0 %<% n2) @?= Boolean False
  interpretJS (s1 %<% n2) @?= Boolean False
  interpretJS (n1 %<% s1) @?= Boolean False
  interpretJS (n1 %<% f)  @?= Boolean False
  interpretJS (num 0 %<% t) @?= Boolean True
  interpretJS (f %<% n1)  @?= Boolean True
  interpretJS (t %<% n1)  @?= Boolean True
  interpretJS (f %<% s0)  @?= Boolean False
  interpretJS (t %<% s0)  @?= Boolean False
  interpretJS (s0 %<% f)  @?= Boolean False
  interpretJS (s0 %<% t)  @?= Boolean False

testLog2 :: Property
testLog2 =
  property $ do
    numb <- forAll $ Gen.enum (0 :: Int) (maxBound `div` 2)
    let real = ceiling (logBase (2 :: Double) (fromIntegral numb)) :: Int
    Number (Int32 real) === interpretJS1 (Number $ Int32 numb) log2

testStrReplicate :: Property
testStrReplicate =
  property $ do
    s <- forAll $ Gen.list (Range.linear 0 20) Gen.alpha
    cnt <- forAll $ Gen.enum 0 30
    Str (concat $ replicate cnt s) ===
      interpretJS2 (Str s) (Number $ Int32 cnt) strReplicate

task3TestGroup :: TestTree
task3TestGroup =
  testGroup
    "Task3"
    [ testCase     "Const"     testConst
    , testProperty "Var-val"   testVarValConversion
    , testProperty "Nan prop"  testOpNanProp
    , testCase     "Test sum"  testSum
    , testCase     "Test sub"  testSub
    , testCase     "Test mul"  testMul
    , testCase     "Test div"  testDiv
    , testCase     "Test le"   testLe
    , testProperty "Log2"      testLog2
    , testProperty "Replicate" testStrReplicate
    ]
