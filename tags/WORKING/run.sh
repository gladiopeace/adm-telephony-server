#!/bin/sh
java -Xmx1024M -server -cp .:log4j.properties:adm-telephony-server.jar:lib/* com.admtel.telephonyserver.core.AdmTelephonyServer