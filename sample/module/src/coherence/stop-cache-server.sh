#!/bin/sh

PID=`ps -ef | grep Dtangosol.coherence.member=$MEMBER | grep -v grep | awk '{print $2}'`

kill $PID

exit 0
