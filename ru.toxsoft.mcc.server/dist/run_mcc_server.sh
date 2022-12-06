#!/bin/sh

# export JAVA_HOME=/usr/java/jdk1.8.0_211-amd64
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export WILDFLY_HOME=/home/dmitry/works/tswildfly-26.1.1.Final/bin/
export LC_ALL=ru_RU.UTF-8

# Префикс проекта
export PROJECT=mcc

# Имя узла кластера
# export NODE=local
export NODE=node01

cd ${WILDFLY_HOME}

# Удаление мусора в рамках подготовки запуска узла кластера
rm -Rfv ../standalone-${PROJECT}-${NODE}/data
rm -Rfv ../standalone-${PROJECT}-${NODE}/tmp

# Запуск сервера
# ./standalone.sh -c standalone-${PROJECT}-${NODE}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE} &
./standalone.sh -c standalone-${PROJECT}-${NODE}.xml -b localhost -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE} --debug *:8787  &
# ./standalone.sh -c standalone-${PROJECT}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE} --debug *:8787 &




