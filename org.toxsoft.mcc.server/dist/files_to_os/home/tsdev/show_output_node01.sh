#!/bin/sh
multitail -cS mcc_log4j -n 10000 -I  /home/tsdev/mcc/tswildfly-18.0.1.Final/standalone-mcc-node01/log/server.log

