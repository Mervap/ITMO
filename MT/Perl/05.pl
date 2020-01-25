while (<>) {
  print if /[x-z].{5,17}[x-z]/;
}
