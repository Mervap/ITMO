module Block2.TestTask1
  ( block2Task1TestGroup
  ) where

import Block1.Task3
import TestHelpers

import Data.Foldable (toList)
import Data.List (sort)
import Hedgehog
import Test.Tasty
import Test.Tasty.Hedgehog

propToFromList :: Property
propToFromList =
  property $ do
    list <- forAll $ genIntList 0 1000
    toList (treeFromList list) === sort list

propFoldMap :: Property
propFoldMap =
  property $ do
    list <- forAll $ genIntList 0 1000
    let tree = treeFromList list
    foldMap (: []) tree === foldr (mappend . (: [])) mempty tree

newtype Composition a =
  Composition (a -> a)

unWrap :: Composition a -> (a -> a)
unWrap (Composition f) = f

instance Semigroup (Composition a) where
  Composition fx <> Composition fy = Composition (fx . fy)

instance Monoid (Composition a) where
  mempty = Composition id

propFoldr :: Property
propFoldr =
  property $ do
    list <- forAll $ genIntList 0 1000
    let tree = treeFromList list
    foldr (:) [] tree === (unWrap $ foldMap (Composition . (:)) tree) []

block2Task1TestGroup :: TestTree
block2Task1TestGroup =
  testGroup
    "Block2.Task1"
    [ testProperty "Test toFromList" propToFromList
    , testProperty "Test tree foldMap property" propFoldMap
    , testProperty "Test tree foldr property" propFoldr
    ]
