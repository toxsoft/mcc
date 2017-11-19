@echo off

:: Домашний каталог s5admin (toolkit)
set S5_ADMIN_HOME=../../../ts-dapps/tool-s5admin
:: Каталог приложения s5admin
set S5_APP_HOME=.

:: Настройка окружения запуска
set S5_ADMIN_CLASS_PATH=%S5_APP_HOME%;%S5_ADMIN_HOME%/s5-admin-cli.jar;%S5_ADMIN_HOME%/libs/*;%S5_APP_HOME%/libs/*;%S5_ADMIN_HOME%/libs/wildfly/*;../dist/*
set S5_ADMIN_USER=root
:: set S5_ADMIN_USER=ts
set S5_ADMIN_PASSWORD=1
set S5_ADMIN_HOST=localhost
:: set S5_ADMIN_HOST=192.168.12.202
  

set S5_ADMIN_PORT=8080
set S5_ADMIN_JMS_PORT=5445
set S5_ADMIN_JMS_THREADS=5
set S5_ADMIN_CONNECT_TIMEOUT=3000
set S5_ADMIN_FAILURE_TIMEOUT=5000
:: set S5_ADMIN_CURRDATA_TIMEOUT=300
set S5_ADMIN_CURRDATA_TIMEOUT=-1
set S5_ADMIN_MODULE=mt-server-deploy
set S5_ADMIN_INTERFACE="ru.toxsoft.mt.server.impl.IMtServerApiRemote"
set S5_ADMIN_BEAN="MtServerApiSessionImpl"
set S5_ADMIN_INITIALIZER="ru.toxsoft.mt.client.connection.MtServicesInitializer"
set S5_ADMIN_CACHED=false
set S5_ADMIN_PROFILER_AGENT=
:: set S5_ADMIN_PROFILER_AGENT=-agentpath:C:\PROGRA~1\JPROFI~1\bin\WINDOW~1\jprofilerti.dll=port=8849,nowait

:: Параметры использования памяти для программы
set S5_XMS_MEMORY=512m
set S5_XMX_MEMORY=1024m

:: Копирование проектно-специфических плагинов
# copy /Y /B "%S5_APP_HOME%\plugins" > %S5_APP_HOME%/log/s5admin.log 

echo java -cp %S5_ADMIN_CLASS_PATH% ru.toxsoft.s5.admin.cli.Main
java %S5_ADMIN_PROFILER_AGENT% -Dfile.encoding=CP866 -Xms%S5_XMS_MEMORY% -Xmx%S5_XMX_MEMORY% -cp %S5_ADMIN_CLASS_PATH% ru.toxsoft.s5.admin.cli.Main login -user %S5_ADMIN_USER% -password %S5_ADMIN_PASSWORD% -host %S5_ADMIN_HOST% -port %S5_ADMIN_PORT% -jmsPort %S5_ADMIN_JMS_PORT% -jmsThreadMaxSize %S5_ADMIN_JMS_THREADS% -connectTimeout %S5_ADMIN_CONNECT_TIMEOUT% -failureTimeout %S5_ADMIN_FAILURE_TIMEOUT% -module %S5_ADMIN_MODULE% -interface %S5_ADMIN_INTERFACE% -bean %S5_ADMIN_BEAN%  -cached %S5_ADMIN_CACHED% -initializer %S5_ADMIN_INITIALIZER%
