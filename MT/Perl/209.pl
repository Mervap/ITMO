while (<>) {
  s/\([^\)]*\)/()/g;
  print;
}