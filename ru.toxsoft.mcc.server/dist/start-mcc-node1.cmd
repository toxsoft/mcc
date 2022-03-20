set JAVA_HOME=C:\Program Files\Java\jdk-11.0.10
set WILDFLY_HOME=C:\works\tswildfly-18.0.1.Final 

:: Префикс проекта
set PROJECT=mcc
:: Имя узла кластера
set NODE=node01
:: Смещение порта
set PORT_OFFSET=0


cd %WILDFLY_HOME%\bin

rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\data
rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\tmp

rem standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET%
standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET%
