{-# LANGUAGE ScopedTypeVariables #-}

module Task2
  ( getTask2TestGroup
  ) where

import Control.Concurrent.Async
import Control.Concurrent.MVar
import Control.Monad ((>=>), join, replicateM)
import Data.IORef
import Data.List (nubBy)
import qualified Data.Map as M
import qualified Data.Set as Set
import GHC.IOArray
import Hedgehog
import qualified Hedgehog.Gen as Gen
import qualified Hedgehog.Range as Range
import Test.Tasty
import Test.Tasty.Hedgehog

import Task2.ConcurrentHashTable
import Task2.MapRequest

genRequest :: Int -> Gen (Request Int Int)
genRequest keyBound = do
  key <- Gen.enum 0 keyBound
  value <- Gen.enum 0 2000
  Gen.element [Lookup key, Insert key value]
  
hasDuplicates :: (Ord a) => [a] -> Bool
hasDuplicates list = length list /= length (Set.fromList list)

assocCHT :: ConcurrentHashTable k v -> IO [(k, v)]
assocCHT cht = do
  takeMVar (onResize cht)
  (dataArr, dataSize) <- readIORef (internalData cht)
  res <- join <$> mapM (readIOArray dataArr >=> readMVar) [0..dataSize - 1]
  putMVar (onResize cht) ()
  return res

checkRes :: IORef (M.Map Int Int) -> ConcurrentHashTable Int Int -> PropertyT IO ()
checkRes defMapRef cht = do
     -- Check that cht subset defMap
    defMapAssocs <- evalIO $ M.assocs <$> readIORef defMapRef
    mapM_ (\(k, a) -> evalIO (getCHT k cht) >>= (Just a ===)) defMapAssocs

    -- Check there aren't any duplicates in the cht
    chtAssocs <- evalIO $ assocCHT cht
    assert $ (not . hasDuplicates) chtAssocs

    -- Check that defMap subset cht
    defMap <- evalIO $ readIORef defMapRef
    mapM_ (\(k, a) -> Just a === M.lookup k defMap) chtAssocs

    -- Check size
    (length chtAssocs ===) =<< evalIO (sizeCHT cht)

propSingleThread :: IORef (M.Map Int Int) -> ConcurrentHashTable Int Int -> Property
propSingleThread defMapRef cht =
  withTests 1000 $
  property $
   do
    -- Run random request
    req <- forAll $ genRequest 2000
    evalIO $ runRequest req defMapRef
    evalIO $ runRequest req cht

    -- Check results
    checkRes defMapRef cht

propMultiThread :: Property
propMultiThread =
  withShrinks 1 $
  property $
   do
    -- Gen random requests
    (defMapRef :: IORef (M.Map Int Int)) <- evalIO $ newIORef M.empty
    (cht :: ConcurrentHashTable Int Int) <- evalIO newCHT
    let listLength = Range.linear 1000 (2000 :: Int)
    req' <- forAll $ Gen.list listLength (genRequest 100000)
    req <- replicateM 6 $ return (nubBy (\a b -> getKey a == getKey b) req')

    -- Run requests
    evalIO $ mapConcurrently_ (mapM (`runRequest` cht)) req
    evalIO $ mapM_ (`runRequest` defMapRef) (concat req)

    -- Check results
    checkRes defMapRef cht

getTask2TestGroup :: IO TestTree
getTask2TestGroup = do
  defMapRef <- newIORef M.empty
  cht <- newCHTWith 10
  return $
    testGroup
      "Task2"
      [ testProperty "Test single thread" $ propSingleThread defMapRef cht
      , testProperty "Test multi threaded"  propMultiThread
      ]
