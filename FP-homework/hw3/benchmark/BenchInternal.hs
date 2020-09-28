module BenchInternal
  ( benchWithReport
  ) where

import Control.Exception.Base (finally)
import Criterion.Main (Benchmark, defaultConfig, defaultMainWith)
import Criterion.Types (Config(..))
import System.Console.ANSI
import System.Directory (createDirectoryIfMissing, getCurrentDirectory)
import System.FilePath ((</>))

-- | Launches the passed 'Benchmark's with html criterion report generation. 
-- It also displays a message about where this report was generated.
-- It's @report@ directory by the execution path
benchWithReport :: String -> [Benchmark] -> IO ()
benchWithReport name benchs = do
  workingDirectory <- getCurrentDirectory
  let reportDirPath = workingDirectory </> "report"
  createDirectoryIfMissing True reportDirPath
  let reportPath = reportDirPath </> name ++ ".html"
  defaultMainWith defaultConfig {reportFile = Just reportPath} benchs
  colorfulReport name reportPath `finally` setSGR []

-- | Displays a color message about where the report file is saved
colorfulReport :: String -> FilePath -> IO ()
colorfulReport name reportPath = do
  setSGR [SetColor Foreground Dull Green, SetConsoleIntensity NormalIntensity]
  putStrLn $ "Report of " ++ name ++ " bench is presented in " ++ reportPath
