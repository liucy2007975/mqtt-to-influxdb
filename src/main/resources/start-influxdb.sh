#!/bin/bash

boot2docker up
boot2docker ip

docker kill $(docker ps -a -q)
docker rm $(docker ps -a -q)

sleep 2
echo "###############################"
echo "# CREATE CONTAINER : INFLUXDB #"
echo "###############################"

docker run -p 8086:8086 -p 8083:8083 -d \
-e PRE_CREATE_DB="domotic" \
tutum/influxdb:latest

sleep 2
echo "##############################"
echo "# CREATE CONTAINER : GRAFANA #"
echo "##############################"

docker run -d -p 81:80 \
-e INFLUXDB_HOST=192.168.59.103 \
-e INFLUXDB_PORT=8086 \
-e INFLUXDB_NAME=domotic \
-e INFLUXDB_USER=root \
-e INFLUXDB_PASS=root \
-e INFLUXDB_IS_GRAFANADB=true \
-e HTTP_USER=admin \
-e HTTP_PASS=admin \
tutum/grafana

docker logs $(docker ps -l -q)
sleep 3
