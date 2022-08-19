@echo off
set db=%1
set user=%2
set password=%3
set tables=%4
set directory=%5

echo db %db%, user %user%, password %password%, tables %tables%, directory %directory%

db2 connect to %db% user %user% using %password%

:loop
for /f "tokens=1* delims=," %%a in ("%tables%") do (
  rem db2look -d %db% -e -t %%a -i %user% -w %password% > %directory%\%%a.ddl
  echo %%a
  db2 export to %%a.ixf of ixf select * from %%a
  set tables=%%b
)

if defined tables goto loop