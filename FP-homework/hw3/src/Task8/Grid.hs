module Task8.Grid
  ( Grid(..)
  , up
  , down
  , left
  , right
  , gridRead
  , gridWrite
  , horizontal
  , vertical
  ) where

import Control.Comonad

import Task8.Zipper

newtype Grid a =
  Grid
    { unGrid :: ListZipper (ListZipper a)
    }

up :: Grid a -> Grid a
up (Grid g) = Grid (listLeft g)

down :: Grid a -> Grid a
down (Grid g) = Grid (listRight g)

left :: Grid a -> Grid a
left (Grid g) = Grid (fmap listLeft g)

right :: Grid a -> Grid a
right (Grid g) = Grid (fmap listRight g)

gridRead :: Grid a -> a
gridRead (Grid g) = extract $ extract g

gridWrite :: a -> Grid a -> Grid a
gridWrite x (Grid g) = Grid $ listWrite newLine g
  where
    oldLine = extract g
    newLine = listWrite x oldLine

horizontal :: Grid a -> ListZipper (Grid a)
horizontal = genericMove left right

vertical :: Grid a -> ListZipper (Grid a)
vertical = genericMove up down

instance Functor Grid where
  fmap f (Grid zp) = Grid ((f <$>) <$> zp)

instance Comonad Grid where
  extract = gridRead
  duplicate = Grid . fmap horizontal . vertical
