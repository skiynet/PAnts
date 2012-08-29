#!/bin/sh

SQLFILE=$1

echo ' / as sysdba'
echo 'set pages 200'
echo 'SET FEEDBACK OFF'

echo @$SQLFILE
