sub find_host {
  my ($href) = @_;

  $href =~ /(?<scheme>\S+?:\/\/)?(\S*?\@)?(?<host>[^:\/\?#\s]+)(:(?<port>\d+))?/i;
  $host = $+{host};
  $scheme = $+{scheme};
  $port = ${port};
  
  $empty_reg = qr/^\s*$/i;
  if ($host =~ $empty_reg || ($scheme =~ $empty_reg && $port =~ $empty_reg)) {
    return undef;
  }
  else {
    return lc $host;
  }
}

my %ans;
my $input;

while (<>) {
  $input = $input . $_;
} 

$input =~ s/\n//g;
while ($input =~ m/<\s*a.+?href\s*=\s*"(.+?)".*?>/i) {
  $new_el = find_host($1);   
  if (defined $new_el) {
    $ans{$new_el} = 1;
  }
  $input =~ s/<\s*a.+?href\s*=\s*"\s*(.+?)\s*".*?>//i;
}

print join "\n", sort (keys %ans);
