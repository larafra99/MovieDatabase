# Database with Postgre

A small application in java to query a table at a postgre database

## Compile code
```
javac -d out src\main\java\com\moviedatabase\DatabaseConnection.java src\main\java\com\moviedatabase\TableContentViewer.java
```
## Run code
download the Postgre JDBC Driver from https://jdbc.postgresql.org/download/ and add it to the start command ie
```
java -cp "out;postgresql-42.7.7.jar" com.moviedatabase.TableContentViewer 
```

#  Postgre Database 
an external postgreSQL is required to run this application.

## Installing Postgre
usually, Postgre is available via your package manager on Linux
for alternate downloads see: https://www.postgresql.org/download/ 

## Configuring Postgre
external connections typically need to be enabled at postgresql.conf
```text
listen_addresses = '*'
```
user authentication needs to be enabled at your local network at pg_hba.conf
```text
host    all     all     192.168.x.y/24      scram-sha-256
```

## Start/Stop Postgre
If Postgre is installed on Linux, use systemd for start/stop operations
### Start
```text
systemctl start postgresql.service
```
### Stop
```text
systemctl stop postgresql.service
```
### Status
```text
systemctl status postgresql.service
```
###


