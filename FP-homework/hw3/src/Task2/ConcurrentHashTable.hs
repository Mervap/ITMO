module Task2.ConcurrentHashTable
  ( -- * The @ConcurrentHashTable@ type.
    ConcurrentHashTable(..)

    -- * Querying to @ConcurrentHashTable@
  , newCHT
  , newCHTWith
  , getCHT
  , putCHT
  , sizeCHT
  ) where

import Control.Concurrent
import Control.Monad ((>=>), when)
import Data.Hashable
import Data.IORef
import Data.List (lookup)
import GHC.IOArray
import GHC.IO (catchAny)

-- | Hash-table from keys @k@ to values @v@, based on 'MVar',
-- using buckets and 'IORef'. Rehashing doesn't block read operations
data ConcurrentHashTable k v =
  ConcurrentHashTable
    { internalData :: IORef (DataArray k v, Int) -- ^ Array of buckets with buckets cnt
    , elementsCnt  :: IORef Int                  -- ^ Number of elements in the table
    , onResize     :: MVar ()                    -- ^ Whether the table is in the rehashed state
    }

type DataArray k v = IOArray Int (MVar [(k, v)])

-- | Creates new 'ConcurrentHashTable' with default size
newCHT :: IO (ConcurrentHashTable k v)
newCHT = newCHTWith 100

-- | Creates new 'ConcurrentHashTable' with user defined size
newCHTWith :: Int -> IO (ConcurrentHashTable k v)
newCHTWith initSize = do
  arr <- initNewLocks initSize
  elemCnt <- newIORef 0
  resizeLock <- newMVar ()
  arrRef <- newIORef (arr, initSize)
  return $ ConcurrentHashTable arrRef elemCnt resizeLock

-- | Lookup the value at a key in the 'ConcurrentHashTable'.
-- The function will return the corresponding value as ('Just' value),
-- or 'Nothing' if the key isn't in the map
getCHT :: (Hashable k, Eq k) => k -> ConcurrentHashTable k v -> IO (Maybe v)
getCHT key cht = do
  mVar <- readBucket cht $ hash key
  list <- readMVar mVar
  return $ lookup key list

-- | Insert a new key and value in the 'ConcurrentHashTable'.
-- If the key is already present in the map, the associated value is replaced with
-- the supplied value
putCHT :: (Hashable k, Eq k) => k -> v -> ConcurrentHashTable k v -> IO ()
putCHT key value cht = do
  elemCnt <- sizeCHT cht
  (_, dataSize) <- readIORef (internalData cht)
  if elemCnt < 4 * dataSize `div` 3  -- Rehash if elems >= 4/3 * size
    then putData
    else resize dataSize cht >>= \x ->
           if x
             then putData
             else putCHT key value cht
  where
    putData = do
      bucket <- readBucket cht $ hash key
      oldVal <- readMVar bucket
      (do new <- modifyMVar bucket (return . modifyBucket)
          when new $ atomicModifyIORef' (elementsCnt cht) (\x -> (x + 1, ()))) `catchAny`
       const (modifyMVar bucket (const $ return (oldVal, ())))
    modifyBucket [] = ([(key, value)], True)
    modifyBucket ((k, v):xs) =
      if k == key
        then ((key, value) : xs, False)
        else let (kk, bb) = modifyBucket xs in ((k, v) : kk, bb)

-- | Number of elements in the 'ConcurrentHashTable'
sizeCHT :: ConcurrentHashTable k v -> IO Int
sizeCHT = readIORef . elementsCnt

initNewLocks :: Int -> IO (DataArray k v)
initNewLocks size = do
  baseElem <- newEmptyMVar
  arr <- newIOArray (0, size - 1) baseElem
  mapM_ (initElem arr) [0..size - 1]
  return arr
  where
    initElem arr ind = do
      emptyElem <- newMVar []
      writeIOArray arr ind emptyElem

readBucket :: ConcurrentHashTable k v -> Int -> IO (MVar [(k, v)])
readBucket cht keyHash = do
  (arr, arrSize) <- readIORef (internalData cht)
  readIOArray arr (keyHash `mod` arrSize)

resize :: (Hashable k) => Int -> ConcurrentHashTable k v -> IO Bool
resize oldSize cht = do
  ok <- tryTakeMVar (onResize cht)
  case ok of
    Nothing -> threadDelay 10 >> return False
    Just _ ->
      (do (_, curSize) <- readIORef (internalData cht)  -- Perhaps, already rehashed
          when (oldSize == curSize) (resizeInternal oldSize cht)
          putMVar (onResize cht) ()
          return True) `catchAny`
      const (tryPutMVar (onResize cht) () >> return False)

resizeInternal :: (Hashable k) => Int -> ConcurrentHashTable k v -> IO ()
resizeInternal oldSize cht = do
  let newSize = 2 * oldSize
  newData <- initNewLocks newSize
  (oldData, _) <- readIORef (internalData cht)
  mapM_ (readIOArray oldData >=> rehashBucket newData newSize) [0 .. oldSize - 1]
  atomicWriteIORef (internalData cht) (newData, newSize)  -- Works as 2-CAS
  where
    rehashBucket :: (Hashable k) => DataArray k v -> Int -> MVar [(k, v)] -> IO ()
    rehashBucket newData newSize bucket =
      readMVar bucket >>= mapM_ (rehashElem newData newSize)
    rehashElem :: (Hashable k) => DataArray k v -> Int -> (k, v) -> IO ()
    rehashElem newData newSize (k, v) = do
      newBucket <- readIOArray newData (hash k `mod` newSize)
      modifyMVar_ newBucket $ \bucket -> return ((k, v) : bucket)
