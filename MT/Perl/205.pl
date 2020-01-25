while (<>) {
  s/(^|\W+)(\w)(\w)/\1\3\2/g;
  print;
}