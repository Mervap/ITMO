while (<>) {
  print if /\([^\(\)]*\w+[^\(\)]*\)/;
}