#!/bin/sh

PID=`ps -ef | grep Dtangosol.coherence.member=$MEMBER | grep -v grep | awk '{print $2}'`

if [ "$PID" != "" ]; then
  echo "cache-server is already running."
  exit 0
fi

./cache-server.sh > cache-server-${MEMBER}.log 2>&1 &

exit 0
	