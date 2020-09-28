module Task8.Comonad19
  ( -- * Animation @Config@ type
    Config(..)
  , defaultConfig

    -- Animating simulating infection propagation
  , animate
  , animateWith
  ) where

import Control.Comonad
import Control.Concurrent (threadDelay)
import Control.Monad
import System.Console.ANSI
import System.Random

import Task8.Grid
import Task8.Zipper
import Data.List (foldl')

-- | Animation configuration type
data Config =
  Config
    { infectionProb    :: Double  -- ^ Infection probability
    , incubationPeriod :: Int     -- ^ Incubation period
    , illnessPeriod    :: Int     -- ^ Illness period
    , immunityPeriod   :: Int     -- ^ Immunity period
    , width            :: Int     -- ^ Animation field width
    , height           :: Int     -- ^ Animation field height
    , delay            :: Int     -- ^ Pause between infection propagation drawing steps (ms)
    }
  deriving (Eq, Read, Show)


-- | Some default config values
defaultConfig :: Config
defaultConfig =
  Config
    { infectionProb    = 0.15
    , incubationPeriod = 5
    , illnessPeriod    = 6
    , immunityPeriod   = 17
    , width            = 35
    , height           = 15
    , delay            = 100
    }

-- | The infection status of each individual 'Person'
data State
  = Healthy Int   -- ^ The person is healthy and has @n@ days of immunity
  | Infected Int  -- ^ The person is infected, symptoms will appear in @n@ days
  | Ill Int       -- ^ The person is ill and will recover in @n@ days

-- | The person with personal random generator
data Person =
  Person
    { randGen :: StdGen
    , state   :: State
    }

-- | Default person (for cells outside the visible grid)
defPerson :: Person
defPerson = Person (mkStdGen 0) (Healthy 0)

-- | Default rows (outside the visible grid)
defPersonLZ :: ListZipper Person
defPersonLZ = LZ (repeat defPerson) defPerson (repeat defPerson)

genColumn :: Bool -> Config -> IO (ListZipper Person)
genColumn isCoreInfected config = do
  let h = height config
  l <-
    mapM
      (const $ randomIO >>= \r -> return $ Person (mkStdGen r) (Healthy 0))
      [1..h `div` 2]
  seed <- randomIO
  let cPerson = corePerson seed
  r <-
    mapM
      (const $ randomIO >>= \r -> return $ Person (mkStdGen r) (Healthy 0))
      [1..h `div` 2]
  return $ LZ (l ++ repeat defPerson) cPerson (r ++ repeat defPerson)
  where
    corePerson :: Int -> Person
    corePerson seed =
      if isCoreInfected
        then Person (mkStdGen seed) (Infected (incubationPeriod config))
        else Person (mkStdGen seed) (Healthy 0)

genColumns :: Config -> IO (Grid Person)
genColumns config = do
  let w = width config
  l <- mapM (const $ genColumn False config) [1..w `div` 2]
  m <- genColumn True config
  r <- mapM (const $ genColumn False config) [1..w `div` 2]
  return $ Grid (LZ (l ++ repeat defPersonLZ) m (r ++ repeat defPersonLZ))

generateGrid :: Config -> IO (Grid Person)
generateGrid config = do
  let w = height config
  let h = width config
  when (even w || even h) (error "Height & width must be the odd numbers")
  genColumns config

neighbours :: [Grid a -> Grid a]
neighbours = [left, right, up, down]

-- | 'True' if one person infects the another after contact. 'False' otherwise
isNeighborPassedInf :: Config -> Int -> Int -> Grid Person -> Bool
isNeighborPassedInf config rnd mxRnd g =
  case state (extract g) of
    Ill      _ -> isPassed
    Infected _ -> isPassed
    Healthy  _ -> False
  where
    isPassed = fromIntegral (abs rnd) / fromIntegral mxRnd < infectionProb config

-- | Rule for one step spread of infection
stepRule :: Config -> Grid Person -> Person
stepRule config gr = Person newGen newState
  where
    person = extract gr
    oldGen = randGen person
    (newState, newGen) =
      case state person of
        Ill 1      -> (Healthy (immunityPeriod config), oldGen)
        Ill n      -> (Ill (n - 1), oldGen)
        Infected 1 -> (Ill (illnessPeriod config), oldGen)
        Infected n -> (Infected (n - 1), oldGen)
        Healthy  0 -> do
          let (isInfected, newG) = foldl' checkNeighbour (False, oldGen) neighbours
          if isInfected
            then (Infected (incubationPeriod config), newG)
            else (Healthy 0, newG)
        Healthy n  -> (Healthy (n - 1), oldGen)
    checkNeighbour :: (Bool, StdGen) -> (Grid Person -> Grid Person) -> (Bool, StdGen)
    checkNeighbour (is, gen) neighbour = do
      let (rnd, newG) = next gen
      let (_, mx)     = genRange gen
      (is || isNeighborPassedInf config rnd mx (neighbour gr), newG)

-- | One step spread of infection
step :: Config -> Grid Person -> Grid Person
step config = extend (stepRule config)


-- | Pretty print the 'Grid' of 'Person'
printGrid :: Config -> Grid Person -> IO ()
printGrid config gr = do
  let zp = unGrid gr
  let grid = (`toList` width config) <$> toList zp (height config)
  mapM_ printRow grid
  where
    printRow :: [Person] -> IO ()
    printRow lst = do
      mapM_ (printPerson . state) lst
      putStrLn ""
    printPerson :: State -> IO ()
    printPerson (Healthy 0) = do
      setSGR [Reset]
      putStr "."
    printPerson (Healthy _) = do
      setSGR [SetColor Foreground Dull Green, SetConsoleIntensity NormalIntensity]
      putStr "@"
    printPerson (Infected _) = do
      setSGR [SetColor Foreground Vivid Red, SetConsoleIntensity BoldIntensity]
      putStr "#"
    printPerson (Ill _) = do
      setSGR [SetColor Foreground Dull Red, SetConsoleIntensity NormalIntensity]
      putStr "#"

-- | Animate spread of infection with default config
animate :: IO ()
animate = animateWith defaultConfig

-- | Animate spread of infection with user config
animateWith :: Config -> IO ()
animateWith conf = do
  clearScreen
  stGr <- generateGrid conf
  animateLoop stGr
  where
    animateLoop :: Grid Person -> IO ()
    animateLoop gr = do
      setCursorPosition 0 0
      printGrid conf gr
      let newGr = step conf gr
      threadDelay (delay conf * 1000)
      animateLoop newGr
