while (<>) {
  s/(\d+)0(\D+)/\1\2/g;
  print;
}