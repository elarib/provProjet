#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

INPUT=$1
OUTPUT=$2
PROCESS_NAME=$3
PID=$4
OPERATION=$5
PATH_SUFFIX=$6


#! echo mvn -f $DIR/pom.xml exec:java -Dexec.mainClass=fr.univamu.m2sir.windowsprocessprov.main.Main -Dexec.args=\""$INPUT $DIR/frontend/$OUTPUT $PROCESS_NAME $PID $OPERATION $PATH_SUFFIX\"" -Dexec.cleanupDaemonThreads=false;
mvn -f $DIR/pom.xml exec:java -Dexec.mainClass=fr.univamu.m2sir.windowsprocessprov.main.Main -Dexec.args="$INPUT $DIR/frontend/$OUTPUT $PROCESS_NAME $PID $OPERATION $PATH_SUFFIX" -Dexec.cleanupDaemonThreads=false

exit 1