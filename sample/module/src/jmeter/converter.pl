#!/usr/bin/perl

$timegrid = 1000;

while (<>) {
	($start_time, $elapsed_time, $url, $return_code) = split(/,/);

	$grid_begin = int($start_time / $timegrid);
	$grid_end = int(($start_time + $elapsed_time) / $timegrid);

	for ($g = $grid_begin; $g <= $grid_end; $g++) {
		$concurrency{$g}++;
	}
	$throughput{$grid_end}++;
}

print "time(ms),concurrency(time grid=" . $timegrid . "ms),throughput(tps)\n";

foreach (sort keys %concurrency) {
	$throughput{$_} = 0 if ($throughput{$_} eq '');
	printf "%d,%d,%d\n", $_ * $timegrid ,$concurrency{$_}, $throughput{$_};
}
