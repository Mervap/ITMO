{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE MultiParamTypeClasses #-}

module Task2.MapRequest
  ( -- * The @Request@ type.
    Request(..)

    -- * The @TestableMap@ class type.
  , TestableMap(..)
  , getKey
  ) where

import Control.Monad (void)
import Control.Monad.STM (atomically)
import Data.Hashable (Hashable)
import Data.IORef
import qualified Data.Map as M
import qualified StmContainers.Map as SM

import Task2.ConcurrentHashTable

-- | Type of request to the key-values map
data Request k v
  = Insert !k v  -- ^ Insert operation
  | Lookup !k    -- ^ Lookup operation
  deriving (Show, Eq)

-- | Get key from 'Request'
getKey :: Request k v -> k
getKey (Lookup k) = k
getKey (Insert k _) = k

-- | The TestableMap class is used for types that can be tested by 'Request' operations
class TestableMap tMap k v where
  runRequest :: Request k v -> tMap -> IO ()

instance (Hashable k, Eq k) => TestableMap (ConcurrentHashTable k v) k v where
  runRequest (Lookup k) s = void $ getCHT k s
  runRequest (Insert k a) s = putCHT k a s

instance (Hashable k, Eq k) => TestableMap (SM.Map k v) k v where
  runRequest (Lookup k) m = void $ atomically (SM.lookup k m)
  runRequest (Insert k v) m = atomically (SM.insert v k m)

instance (Ord k) => TestableMap (IORef (M.Map k v)) k v where
  runRequest (Lookup k) m = readIORef m >>= void . return . M.lookup k
  runRequest (Insert k v) m =
    atomicModifyIORef' m $ \mp -> (M.insert k v mp, ())