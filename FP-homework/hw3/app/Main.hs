module Main where

import Control.Monad (unless, when)
import Data.IORef (modifyIORef, newIORef, readIORef)
import Options.Applicative

import Task8.Comonad19

configParser :: Parser Config
configParser =
  Config <$>
  option
    auto
    (long "infProb" <> short 'p' <> metavar "INF_PROB" <> showDefault <>
     value (infectionProb defaultConfig) <>
     help "Infection probability. Fractional number 0 < p < 1") <*>
  option
    auto
    (long "incPer" <> metavar "INC_PERIOD" <> showDefault <>
     value (incubationPeriod defaultConfig) <>
     help "Incubation period. Integer number i > 0") <*>
  option
    auto
    (long "illPer" <> metavar "ILL_PERIOD" <> showDefault <>
     value (illnessPeriod defaultConfig) <>
     help "Illness period. Sets how long the illness lasts. Integer number i > 0") <*>
  option
    auto
    (long "immPer" <> metavar "IMM_PERIOD" <> showDefault <>
     value (immunityPeriod defaultConfig) <>
     help "Immunity period. Sets how long the immunity lasts. Integer number i > 0") <*>
  option
    auto
    (long "width" <> short 'w' <> metavar "WIDTH" <> showDefault <>
     value (width defaultConfig) <>
     help "Sets the field width. Integer number i > 0") <*>
  option
    auto
    (long "height" <> short 'h' <> metavar "HEIGHT" <> showDefault <>
     value (height defaultConfig) <>
     help "Sets the field height. Integer number i > 0") <*>
  option
    auto
    (long "delay" <> short 'd' <> metavar "DELAY" <> showDefault <>
     value (height defaultConfig) <>
     help "Sets a pause between infection propagation drawing steps (ms)")

validateConfig :: Config -> IO Bool
validateConfig (Config p inc ill imm w h d) = do
  rf <- newIORef (0 :: Int)
  when (p <= 0 || p >= 1) $
    putStrLn "Probability must be in (0, 1)" >> modifyIORef rf (+ 1)
  when (inc < 1) $
    putStrLn "Incubation period must be greater then 0" >> modifyIORef rf (+ 1)
  when (ill < 1) $
    putStrLn "Illness period must be greater then 0" >> modifyIORef rf (+ 1)
  when (imm < 1) $
    putStrLn "Immunity period must be greater then 0" >> modifyIORef rf (+ 1)
  when (w < 1 || even w) $
    putStrLn "Width must be odd number greater then 0" >> modifyIORef rf (+ 1)
  when (h < 1 || even h) $
    putStrLn "Height must be odd number greater then 0" >> modifyIORef rf (+ 1)
  when (d < 0) $ 
    putStrLn "Delay must be greater then 0" >> modifyIORef rf (+ 1)
  act <- readIORef rf
  return $ act > 0

-- | Wrapper to start the animation the spread of infection
main :: IO ()
main = do
  config <- execParser opts
  notValid <- validateConfig config
  unless notValid $ animateWith config
  where
    opts =
      info
        (configParser <**> helper)
        (fullDesc <>
         progDesc "Simulates the spread of infection in a specific model" <>
         header "Comonad-19")
