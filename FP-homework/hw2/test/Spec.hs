import VFSTest
import VCSTest

import Test.Tasty

main :: IO ()
main =
  defaultMain $
  testGroup
    "Tests"
    [ vfsTestGroup , vcsTestGroup]
