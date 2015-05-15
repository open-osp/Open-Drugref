#!/bin/sh

# This script is used to setup the config to run the server / tomcat.

export WORKING_ROOT=`pwd`

export CATALINA_BASE=${WORKING_ROOT}/catalina_base

MEM_SETTINGS=" -Xms512m -Xmx512m -Xss512k -XX:NewSize=64m -XX:MaxNewSize=64m -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60 "
export JAVA_OPTS="-Djava.awt.headless=true -server -Xincgc -Xshare:off  -Duser.timezone=GMT  -Dorg.apache.el.parser.COERCE_TO_ZERO=false -Dorg.apache.cxf.Logger=org.apache.cxf.common.logging.Log4jLogger -Dlog4j.override.configuration=${WORKING_ROOT}/override_log4j.xml "${MEM_SETTINGS}

export MAVEN_OPTS=${MEM_SETTINGS}
