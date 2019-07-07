# Roger Server

## Prerequisites

- A JRE 11
- A MongoDB database

## Installation

Fill the configuration parameters in application.properties

Create the following environment variables :
- `ROGER_TOKEN` : A secreat token that will be shared between client and server
- `SPRING_DATA_MONGODB_DATABASE` : Name of Mongo database
- `SPRING_DATA_MONGODB_HOST` : Host of Mongo database
- `SPRING_DATA_MONGODB_PORT` : Port of Mongo database
- `SPRING_DATA_MONGODB_USERNAME` : Username of Mongo database
- `SPRING_DATA_MONGODB_PASSWORD` : Password of Mongo database

Run the Maven command : `mvn clean install spring-boot:run`