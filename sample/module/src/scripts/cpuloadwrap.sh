#!/bin/sh

CNT=$1

i=0
while  [ $i -lt $CNT ]
do
  modules/scripts/cpuload.sh &
  (( i=i+1 ))
done

while true
do
  sleep 10
done
