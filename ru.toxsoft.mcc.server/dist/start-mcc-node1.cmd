set JAVA_HOME=C:\Program Files\Java\jdk-17.0.4.1
set WILDFLY_HOME=d:\works\tswildfly-26.1.1.Final 

:: project prefix
set PROJECT=mcc
:: cluster node name
set NODE=node01
:: port offest
set PORT_OFFSET=0
:: private interface (cluster & management)
set PRIVATE_INTEFACE=192.168.1.105


cd %WILDFLY_HOME%\bin

rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\data
rmdir /S /Q ..\standalone-%PROJECT%-%NODE%\tmp

chcp 1251
echo -------------------------------------------------------------------------------
echo %date% %time% starting standalone RETCODE: %ERRORLEVEL% >> toxsoft-%PROJECT%.log
echo project = %PROJECT%, NODE = %NODE%, private interface = %PRIVATE_INTEFACE%
echo -------------------------------------------------------------------------------
timeout 3

standalone.bat -c standalone-%PROJECT%-%NODE%.xml -b=0.0.0.0 -Djboss.server.base.dir=../standalone-%PROJECT%-%NODE% -Djboss.server.name=%PROJECT%.server.%NODE% -Djboss.bind.address.private=%PRIVATE_INTEFACE% -Djboss.socket.binding.port-offset=%PORT_OFFSET% -Dfile.encoding=CP866

echo standalone RETCODE: %ERRORLEVEL%
echo %date% %time% exit standalone RETCODE: %ERRORLEVEL% >> toxsoft-%PROJECT%.log
pause
