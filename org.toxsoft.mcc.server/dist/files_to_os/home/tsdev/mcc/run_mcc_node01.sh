#!/bin/sh

# export JAVA_HOME=/usr/java/jdk1.8.0_211-amd64
export JAVA_HOME=/usr/java/jdk-11.0.10
export WILDFLY_HOME=/home/tsdev/mm/tswildfly-18.0.1.Final/bin/
export LC_ALL=ru_RU.UTF-8

# 2020-06-22 jprofiler
# JAVA_OPTS="-agentpath:/home/tsdev/tm2/tswildfly-18.0.1.Final/bin/jprofiler10/bin/linux-x64/libjprofilerti.so=port=8849,nowait $JAVA_OPTS"
# export JAVA_OPTS

# Префикс проекта
export PROJECT=mcc

# Имя узла кластера
# export NODE=local
export NODE=node01

# Смещение порта (для запуска нескольких экземпляров на одном сервере
export PORT_OFFSET=0

# Отключение записи хранимых данных (аварийный запуск)
export WRITE_HISTDATA_DISABLED=false

# Отладочный режим блокировок.
export LOCK_TRACE_ENABLE=false


cd ${WILDFLY_HOME}

# Удаление мусора в рамках подготовки запуска узла кластера
rm -Rfv ../standalone-${PROJECT}-${NODE}/data
rm -Rfv ../standalone-${PROJECT}-${NODE}/tmp
# rm -Rfv ../standalone-${PROJECT}-${NODE}/log/server.log

# Запуск сервера
# ./standalone.sh -c standalone-${PROJECT}-${NODE}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE} &
# ./standalone.sh -c standalone-${PROJECT}.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE} -Djboss.server.name=${PROJECT}_${NODE} --debug *:8787 &

# 2021-01-29 mvk test configuration.
# eval "./standalone.sh                                            \
#      -c standalone-${PROJECT}-${NODE}.xml -b 0.0.0.0            \
#      -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE}   \
#      -Djboss.server.name=${PROJECT}_${NODE}                     \
#      -Djboss.socket.binding.port-offset=${PORT_OFFSET}          \
#      -Ds5.lock.tracing=${LOCK_TRACE_ENABLE}                     \
#      -Ds5.sequence.write.disabled=${WRITE_HISTDATA_DISABLED}    \
#      --debug *:8787  &"

eval "./standalone.sh                                          \
    -c standalone-${PROJECT}-${NODE}.xml -b 0.0.0.0            \
    -Djboss.server.base.dir=../standalone-${PROJECT}-${NODE}   \
    -Djboss.server.name=${PROJECT}_${NODE}                     \
    -Djboss.socket.binding.port-offset=${PORT_OFFSET}          \
    -Ds5.lock.tracing=${LOCK_TRACE_ENABLE}                     \
    -Ds5.sequence.write.disabled=${WRITE_HISTDATA_DISABLED}    \
    --debug *:8787 & "
