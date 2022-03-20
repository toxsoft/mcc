@echo off

:: set JAVA_HOME=C:\Program Files\Java\jdk-11.0.10
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.2

:: Настройка окружения запуска
set ADMIN_CLASSPATH=^
../../../ts3-targets/ts3-target-extlibs/lib/*;^
../../../ts3-targets/ts3-target-core/lib/*;^
../../../ts3-targets/ts3-target-uskat/lib/*;^
../../../ts3-targets/ts3-target-sitrol/lib/*
:: ../dist/*

set ADMIN_PLUGINPATH=^
../../../ts3-targets/ts3-target-uskat/main:^
../../../ts3-targets/ts3-target-sitrol/rcp/plugins

set ADMIN_USER=root
set ADMIN_PASSWORD=1
set ADMIN_HOST="localhost"
set ADMIN_PORT=8080
set ADMIN_CONNECT_TIMEOUT=300000
set ADMIN_FAILURE_TIMEOUT=500000
set ADMIN_CURRDATA_TIMEOUT=-1
set ADMIN_INITIALIZER=""


:: Параметры jvm
set _CLASS_PATH=-cp %ADMIN_CLASSPATH%
set _PLUGIN_PATHS=-Dru.uskat.s5.admin.plugin.paths=%ADMIN_PLUGINPATH%
set _MAIN_CLASS=ru.uskat.s5.admin.cli.Main
set _XMS_MEMORY=-Xms2048m
set _XMX_MEMORY=-Xmx2048m
set _CHARSET=-Dfile.encoding=CP866
set _LOGGER=-Dlog4j.configuration=file:log4j.xml
set _REMOTE_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 
set _PROFILER_AGENT=

java %_PLUGIN_PATHS% %_PROFILER_AGENT% %_REMOTE_DEBUG% %_CHARSET% %_LOGGER% %_XMS_MEMORY% %_XMX_MEMORY% %_CLASS_PATH% %_MAIN_CLASS% connect -user %ADMIN_USER% -password %ADMIN_PASSWORD% -host %ADMIN_HOST% -port %ADMIN_PORT% -connectTimeout %ADMIN_CONNECT_TIMEOUT% -failureTimeout %ADMIN_FAILURE_TIMEOUT% -initializer %ADMIN_INITIALIZER%  
