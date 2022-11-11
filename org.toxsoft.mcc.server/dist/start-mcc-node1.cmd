set JAVA_HOME=C:\Program Files\Java\jdk-17.0.4.1
set WILDFLY_HOME=d:\works\tswildfly-26.1.1.Final 

:: Префикс проекта
set PROJECT=mcc
:: Имя узла кластера
set NODE=node01
:: Смещение порта
set PORT_OFFSET=0


cd %WILDFLY_HOME%\bin

rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\data
rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\tmp

chcp 1251
rem standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET%
standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b localhost -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET% -Dfile.encoding=CP866
