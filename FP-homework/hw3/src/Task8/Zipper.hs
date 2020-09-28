module Task8.Zipper
  ( ListZipper(..)
  , listLeft
  , listRight
  , listWrite
  , toList
  , genericMove
  ) where

import Control.Comonad

data ListZipper a =
  LZ [a] a [a]

listLeft :: ListZipper a -> ListZipper a
listLeft (LZ (l:ls) x rs) = LZ ls l (x : rs)
listLeft _ = error "listLeft"

listRight :: ListZipper a -> ListZipper a
listRight (LZ ls x (r:rs)) = LZ (x : ls) r rs
listRight _ = error "listRight"

listWrite :: a -> ListZipper a -> ListZipper a
listWrite x (LZ ls _ rs) = LZ ls x rs

toList :: ListZipper a -> Int -> [a]
toList (LZ ls x rs) n = reverse (take n ls) ++ x : take n rs

iterateTail :: (a -> a) -> a -> [a]
iterateTail f = tail . iterate f

genericMove :: (z a -> z a) -> (z a -> z a) -> z a -> ListZipper (z a)
genericMove f g e = LZ (iterateTail f e) e (iterateTail g e)

instance Functor ListZipper where
  fmap f (LZ ls x rs) = LZ (map f ls) (f x) (map f rs)

instance Comonad ListZipper where
  extract (LZ _ x _) = x
  duplicate = genericMove listLeft listRight
