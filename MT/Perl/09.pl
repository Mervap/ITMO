while (<>) {
  print if /(^[^\s].*[^\s]$)|^\S*$/;
}