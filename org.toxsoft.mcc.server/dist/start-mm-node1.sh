#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export WILDFLY_HOME=/home/tsdev/works/tswildfly-18.0.1.Final/bin/
export LC_ALL=ru_RU.UTF-8

# Префикс проекта
export PROJECT=mm-fgdp

# Имя узла кластера
export NODE=node01

cd ${WILDFLY_HOME}

# Удаление мусора в рамках подготовки запуска узла кластера
rm -Rfv ../standalone-${PROJECT}-${NODE}/data
rm -Rfv ../standalone-${PROJECT}-${NODE}/tmp

# Запуск сервера
# ./standalone.sh -c standalone-${PROJECT}-${NODE}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE}  -Djboss.socket.binding.port-offset=0 
./standalone.sh -c standalone-${PROJECT}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE}  -Djboss.socket.binding.port-offset=0 



