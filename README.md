# CalendarAPI Guide

## Start-Up Guide

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
