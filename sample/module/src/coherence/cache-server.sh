#!/bin/sh

# This will start a cache server


if [ ! -f ${COHERENCE_HOME}/bin/cache-server.sh ]; then
  echo "coherence.sh: must be run from the Coherence installation directory."
  exit
fi

if [ -f $JAVA_HOME/bin/java ]; then
  JAVAEXEC=$JAVA_HOME/bin/java
else
  JAVAEXEC=java
fi

if [[ $1 == '-jmx' ]]; then
    JMXPROPERTIES="-Dcom.sun.management.jmxremote -Dtangosol.coherence.management=all -Dtangosol.coherence.management.remote=true"
    shift
fi

JAVA_OPTS="-Xms$MEMORY -Xmx$MEMORY $JMXPROPERTIES -Dtangosol.coherence.member=$MEMBER -Dtangosol.coherence.distributed.localstorage=true"

$JAVAEXEC -server -showversion $JAVA_OPTS -cp ".:$COHERENCE_HOME/lib/coherence.jar" com.tangosol.net.DefaultCacheServer $1
