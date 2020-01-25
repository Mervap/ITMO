my $f = 0;
my $beg = 1;
while (<>) {
  if (m/^ *$/) {
    $f = 1;
  }
  else {
    if ($f && !$beg) {
      print "\n";
    }
    $beg = 0;
    $f = 0;
    s/(^ +)|( +$)//g;
    s/ {2,}/ /g;
    print $_;
  }
}