#!/bin/bash
echo "Deployment Failed"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR/pom.xml;

INPUT=$1
OUTPUT=$2
PATH_SUFFIX=$3

mvn -f $DIR/pom.xml exec:java -Dexec.mainClass=fr.univamu.m2sir.windowsprocessprov.main.Main -Dexec.args="$INPUT $DIR/frontend/$OUTPUT ? ? ? $PATH_SUFFIX" -Dexec.cleanupDaemonThreads=false

