{-# LANGUAGE InstanceSigs #-}

module Task5 where

data Post =
  Post
    { pTitle :: String
    , pBody  :: String
    }
    deriving Show

data Blog =
  Blog
    { bPosts   :: [Post]
    , bCounter :: Int
    }
    deriving Show

joinBlog :: Blog -> Blog -> Blog
joinBlog (Blog p1 c1) (Blog p2 c2) = Blog (p1 ++ p2) (c1 + c2)

newtype BlogM a =
  BlogM (Blog -> (a, Blog))
  
get :: BlogM a -> (Blog -> (a, Blog))
get (BlogM f) = f

instance Functor BlogM where
  fmap :: (a -> b) -> BlogM a -> BlogM b
  fmap f (BlogM g) =
    BlogM $ \x ->
      let (z, b) = g x
       in (f z, b)

instance Applicative BlogM where
  pure :: a -> BlogM a
  pure = BlogM . (,)
  (<*>) :: BlogM (a -> b) -> BlogM a -> BlogM b
  (BlogM f) <*> (BlogM g) =
    BlogM $ \x ->
      let (z, b) = g x
       in let (z1, b1) = f b
           in (z1 z, b1)

instance Monad BlogM where
  (>>=) :: BlogM a -> (a -> BlogM b) -> BlogM b
  (BlogM f) >>= g =
    BlogM $ \x ->
      let (z, b) = f x
       in let (BlogM h) = g z
           in h b

readPost :: Int -> BlogM Post
readPost ind = BlogM $ \b -> (bPosts b !! ind, b)

newPost :: Post -> BlogM ()
newPost p = BlogM $ \(Blog ph c) -> ((), Blog (p : ph) (c + 1))

post1 :: Post 
post1 = Post "Post1" "Post 1 body"

post2 :: Post 
post2 = Post "Post2" "Post 2 body"

post3 :: Post 
post3 = Post "Post3" "Post 3 body"

post4 :: Post 
post4 = Post "Post4" "Post 4 body"

blog1 :: Blog
blog1 = Blog [post3] 1