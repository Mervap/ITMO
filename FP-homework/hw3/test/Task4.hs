{-# LANGUAGE ScopedTypeVariables #-}

module Task4
  ( task4TestGroup
  ) where

import Test.Tasty
import Test.Tasty.HUnit

import Task3.HalyavaScript
import Task4.HalyavaScriptPrinter

infixr 6 </>

(</>) :: String -> String -> String
a </> b = a <> "\n" <> b

testLog2 :: Assertion
testLog2 =
  jsToString1 log2 @?=
    "function(v0) {"        </>
    "  var v1 = 1"          </>
    "  var v2 = 0"          </>
    "  while ((v1 < v0)) {" </>
    "    v1 = (v1 + v1)"    </>
    "    v2 = (v2 + 1)"     </>
    "  }"                   </>
    "  return v2"           </>
    "}\n"

testSqrt2 :: Assertion
testSqrt2 =
  jsToString1 sqrt2 @?=
    "function(v0) {"                                        </>
    "  var v1 = 0"                                          </>
    "  var v2 = v0"                                         </>
    "  while ((1.0e-5 < (v2 - v1))) {"                      </>
    "    if (((((v2 + v1) / 2) * ((v2 + v1) / 2)) < v0)) {" </>
    "      v1 = ((v2 + v1) / 2)"                            </>
    "    }"                                                 </>
    "    else {"                                            </>
    "      v2 = ((v2 + v1) / 2)"                            </>
    "    }"                                                 </>
    ""                                                      </>
    "  }"                                                   </>
    "  return (((\"Sqrt of \" + v0) + \" is \") + v1)"      </>
    "}\n"

testReplicate :: Assertion
testReplicate =
    jsToString2 strReplicate @?=
      "function(v0, v1) {"   </>
      "  var v2 = \"\""      </>
      "  while ((0 < v1)) {" </>
      "    v2 = (v2 + v0)"   </>
      "    v1 = (v1 - 1)"    </>
      "  }"                  </>
      "  return v2"          </>
      "}\n"

task4TestGroup :: TestTree
task4TestGroup =
  testGroup
    "Task4"
    [ testCase "Log2"      testLog2
    , testCase "Sqrt"      testSqrt2
    , testCase "Replicate" testReplicate
    ]
