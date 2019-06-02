# Java Kafka Spring Example

This project demonstrates how to use Spring and the java language to consumes and read data from Kafka.
It's separated in two subprojects:
* The producer which is a console application taking a filepath argument. This producer will scan each file recursively given in the path, and send a message to Kafka for each file.
* The consumer which is a web application connecting to kafka, and for each received file info sends the data to ElasticSearch

The environment can be set up quickly using the docker compose file (which is a modified version of simplesteph's [file](https://github.com/simplesteph/kafka-stack-docker-compose)).

Don't forget to create the `fileinfo` topic in kafka using the `kafka-topics.sh` script. Please note that the docker-compose `KAFKA_ADVERTISED_LISTENERS` environment variable must be replaced with the docker host IP for the `LISTENER_DOCKER_EXTERNAL` listener.
