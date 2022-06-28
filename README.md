# CalendarAPI Guide

## Preconfigurations
Put a password into your MYSQL ROOT PASSWORD slot in the .env file:
```
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=[PASSWORD FOR MYSQL]
MYSQLDB_DATABASE=calendarapi
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=6868
SPRING_DOCKER_PORT=8080
```

## Start-Up

To Start the application, the user will need to type in their terminal:
```
docker-compose up
```
This will containerize the application and the database into docker containers and connect the two.
The URL for the controller endpoints will begin with :6868.
To close the connection:
```
Ctrl-C
```

## Test endpoints
$URL/api/calendar/test - creates a test user, events, and calendar. Then returns the created calendar to the web page
in JSON.

## Issues installing container
If you are having issues installing and running the container, delete the :5.7 from the mysql portion of the
docker-compose.yml file and it should fix the issues.
```
version: "3.8"
services:
  mysqldb:
    image: mysql: <---- LIKE SO
    restart: unless-stopped
    env_file: ./.env
  ```
