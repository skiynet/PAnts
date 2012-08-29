#!/bin/sh

SLEEP_SEC=$1
SQLFILE=$2

echo ' / as sysdba'
echo 'set pages 200'
echo 'SET FEEDBACK OFF'

while true
do
  echo @$SQLFILE
  sleep $SLEEP_SEC
done
