#!/bin/bash

# Домашний каталог s5admin (toolkit)
S5_ADMIN_HOME=../../../ts-dapps/tool-s5admin
# Каталог приложения s5admin
S5_APP_HOME=.

S5_ADMIN_CLASS_PATH=${S5_APP_HOME}:${S5_ADMIN_HOME}/s5-admin-cli.jar:${S5_ADMIN_HOME}/libs-patches/*:${S5_ADMIN_HOME}/libs/*:${S5_APP_HOME}/libs/*:${S5_ADMIN_HOME}/libs/wildfly/*:../dist/*:${S5_APP_HOME}/libs-app/*
S5_ADMIN_USER=root
S5_ADMIN_PASSWORD=1
S5_ADMIN_HOST=localhost
S5_ADMIN_PORT=8080
S5_ADMIN_JMS_PORT=5445
S5_ADMIN_JMS_THREADS=5
S5_ADMIN_CONNECT_TIMEOUT=3000
S5_ADMIN_FAILURE_TIMEOUT=5000
S5_ADMIN_MODULE=mcc-server-deploy
S5_ADMIN_INTERFACE="ru.toxsoft.mcc.server.impl.IMtServerApiRemote"
S5_ADMIN_BEAN="MccServerApiSessionImpl"
S5_ADMIN_INITIALIZER="ru.toxsoft.mcc.client.connection.MtServicesInitializer"
S5_ADMIN_CACHED=false
S5_ADMIN_PROFILER_AGENT=
# S5_ADMIN_PROFILER=-agentpath:/home/ts/jprofiler-agent/libjprofilerti.so=port=8849,nowait

# Копирование проектно-специфических плагинов
# cp --force "${S5_ADMIN_HOME}/plugins-app/mm.admin.jar" "${S5_APP_HOME}/plugins" > ${S5_APP_HOME}/log/s5admin.log

echo java -cp ${S5_ADMIN_CLASS_PATH} ru.toxsoft.s5.admin.cli.Main
java ${S5_ADMIN_PROFILER} -cp ${S5_ADMIN_CLASS_PATH} ru.toxsoft.s5.admin.cli.Main login -user ${S5_ADMIN_USER} -password ${S5_ADMIN_PASSWORD} -host ${S5_ADMIN_HOST} -port ${S5_ADMIN_PORT} -jmsPort ${S5_ADMIN_JMS_PORT} -jmsThreadMaxSize ${S5_ADMIN_JMS_THREADS} -connectTimeout ${S5_ADMIN_CONNECT_TIMEOUT} -failureTimeout ${S5_ADMIN_FAILURE_TIMEOUT} -module ${S5_ADMIN_MODULE} -interface ${S5_ADMIN_INTERFACE} -bean ${S5_ADMIN_BEAN} -initializer ${S5_ADMIN_INITIALIZER} -cached ${S5_ADMIN_CACHED}
