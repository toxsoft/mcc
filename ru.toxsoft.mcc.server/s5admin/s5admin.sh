@echo off

# Настройка окружения запуска
set ADMIN_CLASSPATH=\
../../../ts3-targets/ts3-target-extlibs/lib/*:\
../../../ts3-targets/ts3-target-core/lib/*:\
../../../ts3-targets/ts3-target-uskat/lib/*:\
../../../ts3-targets/ts3-target-sitrol/lib/*:\
../dist/*

set ADMIN_PLUGINPATH=\
../../../ts3-targets/ts3-target-uskat/main:\
../../../ts3-targets/ts3-target-sitrol/main

ADMIN_USER=root
ADMIN_PASSWORD=1
ADMIN_HOST="localhost"
ADMIN_PORT=8080
ADMIN_CONNECT_TIMEOUT=3000
ADMIN_FAILURE_TIMEOUT=5000
ADMIN_CURRDATA_TIMEOUT=-1
ADMIN_INITIALIZER=""


# Параметры jvm
_CLASS_PATH=-cp %ADMIN_CLASSPATH%
_PLUGIN_PATHS=-Dru.uskat.s5.admin.plugin.paths=%ADMIN_PLUGINPATH%
_MAIN_CLASS=ru.uskat.s5.admin.cli.Main
_XMS_MEMORY=-Xms256m
_XMX_MEMORY=-Xmx512m
_CHARSET=-Dfile.encoding=CP866
_LOGGER=-Dlog4j.configuration=file:log4j.xml
_REMOTE_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 
_PROFILER_AGENT=

java $_PLUGIN_PATHS $_PROFILER_AGENT $_CHARSET $_LOGGER $_XMS_MEMORY $_XMX_MEMORY $_CLASS_PATH $_MAIN_CLASS connect -user $ADMIN_USER -password $ADMIN_PASSWORD -host $ADMIN_HOST -port $ADMIN_PORT -connectTimeout $ADMIN_CONNECT_TIMEOUT -failureTimeout $ADMIN_FAILURE_TIMEOUT -initializer $ADMIN_INITIALIZER  