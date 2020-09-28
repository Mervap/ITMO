{-# LANGUAGE InstanceSigs #-}

module Block1.Task3
  ( 
    -- Property of @Tree@
    isEmpty
  , size
  
    -- * Querying
  , find
  , insert
  , treeFromList
  , erase
  ) where

import Tree

import Data.List as List (group, length, sort)
import Data.List.NonEmpty as NE (NonEmpty (..), head, length, nonEmpty, (<|))
import Data.Maybe (fromJust)

isEmpty :: Tree a -> Bool
isEmpty List = True
isEmpty _    = False

size :: Tree a -> Int
size List = 0
size (Branch leftBranch dataList rightBranch) =
  NE.length dataList + size leftBranch + size rightBranch

find :: Ord a => Tree a -> a -> Maybe a
find List _ = Nothing
find (Branch left (h :| _) right) elemF =
  case compare elemF h of
    LT -> find left elemF
    EQ -> Just h
    GT -> find right elemF

insert :: Ord a => Tree a -> a -> Tree a
insert List x = Branch List (x :| []) List
insert (Branch left dataList right) elemF =
  case compare elemF value of
    LT -> Branch (insert left elemF) dataList right
    EQ -> Branch left (elemF <| dataList) right
    GT -> Branch left dataList (insert right elemF)
  where
    value = NE.head dataList

treeFromList :: (Ord a) => [a] -> Tree a
treeFromList list = fromListGrouped $ group $ sort list
  where
    fromListGrouped :: [[a]] -> Tree a
    fromListGrouped [] = List
    fromListGrouped grouped =
      Branch (fromListGrouped toLeft) (fromJust $ nonEmpty here) (fromListGrouped toRight)
      where
        (toLeft, here:toRight) = splitAt (List.length grouped `div` 2) grouped

erase :: (Ord a) => Tree a -> a -> Tree a
erase List _ = List
erase (Branch left dataList@(value :| hs) right) elemF =
  case compare elemF value of
    LT -> Branch (erase left elemF) dataList right
    GT -> Branch left dataList (erase right elemF)
    EQ ->
      case nonEmpty hs of
        Just xs -> Branch left xs right
        Nothing ->
          case right of
            List -> left
            _    -> Branch left minData treeRest
  where
    (minData, treeRest) = eraseMinNode right
    
    eraseMinNode :: Tree a -> (NonEmpty a, Tree a)
    eraseMinNode List = error "No min element"
    eraseMinNode (Branch List resData rightBranch) = (resData, rightBranch)
    eraseMinNode (Branch leftBranch dataF rightBranch) =
      (leftMinData, Branch leftTreeRest dataF rightBranch)
      where
        (leftMinData, leftTreeRest) = eraseMinNode leftBranch
