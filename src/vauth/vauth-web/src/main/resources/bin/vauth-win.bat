@ECHO OFF

@setlocal EnableDelayedExpansion enableextensions

SET INSTALL_PATH=\Java\Benestra-vauth
cd %INSTALL_PATH%

SET APPNAME=%0
SET NAME=Benestra-vauth

2>NUL CALL :CASE_%1
IF ERRORLEVEL 1 CALL :DEFAULT_CASE

COLOR 0F
EXIT /B
  
:CASE_start
  COLOR AF
  for /F %%a in ('findstr VAUTH_DB_USER "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbuser %%b
  )
  )
  for /F %%a in ('findstr VAUTH_DB_TYPE "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbtype %%b
  )
  )
  for /F %%a in ('findstr VAUTH_DB_HOST "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbhost %%b
  )
  )
  for /F %%a in ('findstr VAUTH_DB_PORT "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbport %%b
  )
  )
  for /F %%a in ('findstr VAUTH_DB_PASS "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbpass %%b
  )
  )
  for /F %%a in ('findstr VAUTH_DB_DATABASE "%INSTALL_PATH%\config\benestra-vauth.conf"') do (
  ECHO.%%a | findstr /V \#>nul && (
	for /F "tokens=2 delims=\= " %%b in ('ECHO %%a') do CALL :dbdatabase %%b
  )
  )
  SET VAUTH_DB_URL=//%VAUTH_DB_HOST%:%VAUTH_DB_PORT%/%VAUTH_DB_DATABASE%
  CALL :dbclass_%VAUTH_DB_TYPE%
  
  ::ECHO %VAUTH_DB_USER%
  ::ECHO %VAUTH_DB_TYPE%
  ::ECHO %VAUTH_DB_HOST%
  ::ECHO %VAUTH_DB_PORT%
  ::ECHO %VAUTH_DB_PASS%
  ::ECHO %VAUTH_DB_DATABASE%
  ::ECHO %VAUTH_DB_URL%
  ::ECHO %VAUTH_DB_CLASS_NAME%

    start java -jar "%INSTALL_PATH%\lib\Benestra-vauth-boot"
  GOTO END_CASE
:CASE_deploy
  COLOR 9F
  ECHO|SET /p="%NAME% is deploying ..."

    copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\src\main\resources\config\strings\*" "%INSTALL_PATH%\strings\"
	copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\src\main\resources\config\mails\*" "%INSTALL_PATH%\mails\"
	copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\src\main\resources\config\Benestra-vauth.conf" "%INSTALL_PATH%\config\"
    copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\src\main\resources\sql\Benestra-vauth.sql" "%INSTALL_PATH%\sql\Benestra-vauth.sql"
    copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\src\main\resources\sql\Benestra-vauth-psql.sql" "%INSTALL_PATH%\sql\Benestra-vauth-psql.sql"
	copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-web\target\Benestra-vauth-web-1.0.0.war" "%INSTALL_PATH%\lib\Benestra-vauth"
	del /qf "%INSTALL_PATH%\lib\libs"
	copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-boot\target\libs" "%INSTALL_PATH%\lib\libs\"
	copy /y "%INSTALL_PATH%\src\Benestra-vauth\Benestra-vauth-boot\target\Benestra-vauth-boot-1.0.0.jar" "%INSTALL_PATH%\lib\Benestra-vauth-boot"
  ECHO success
  GOTO END_CASE
:DEFAULT_CASE
  ECHO "Usage %APPNAME% {start|deploy}"
  GOTO END_CASE
:END_CASE
  VER > NUL # reset ERRORLEVEL
  GOTO :EOF # return from CALL

:dbuser
  SET VAUTH_DB_USER=%1
  GOTO :EOF
:dbtype
  SET VAUTH_DB_TYPE=%1
  GOTO :EOF
:dbhost
  SET VAUTH_DB_HOST=%1
  GOTO :EOF
:dbport
  SET VAUTH_DB_PORT=%1
  GOTO :EOF
:dbpass
  SET VAUTH_DB_PASS=%1
  GOTO :EOF
:dbdatabase
  SET VAUTH_DB_DATABASE=%1
  GOTO :EOF
:dbclass_mysql
  SET VAUTH_DB_CLASS_NAME=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource
  GOTO :EOF
:dbclass_postgresql
  SET VAUTH_DB_CLASS_NAME=org.postgresql.ds.PGSimpleDataSource
  GOTO :EOF
