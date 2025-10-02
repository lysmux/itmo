#!/bin/sh
exec java -DFCGI_PORT=${SERVER_PORT} -jar app.jar