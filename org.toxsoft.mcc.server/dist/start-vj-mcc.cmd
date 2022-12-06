:: This is the start script of work/combat server. Don't edit it for your own purposes!!! 

set JAVA_HOME=C:\Program Files\Java\jdk-17.0.4.1
set WILDFLY_HOME=C:\works\tswildfly-26.1.1.Final 

:: project prefix
set PROJECT=mcc
:: cluster node name
set NODE=node01
:: port offest
set PORT_OFFSET=0


cd %WILDFLY_HOME%\bin

rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\data
rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\tmp

chcp 1251
rem standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b 0.0.0.0 -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET%
rem standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b localhost -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%_%NODE% -Djboss.socket.binding.port-offset=%PORT_OFFSET% -Dfile.encoding=CP866
standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b localhost -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%.server -Djboss.socket.binding.port-offset=%PORT_OFFSET% -Dfile.encoding=CP866
