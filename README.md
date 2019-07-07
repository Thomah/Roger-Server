# Roger Server

## Prerequisites

- A JRE 11
- A MongoDB database

## Installation

Fill the configuration parameters in application.properties

Create the following environment variables :
- `ROGER_TOKEN` : A secreat token that will be shared between client and server
- `MONGODB_URI` : Thr URI to connect to a Mongo Database

Run the Maven command : `mvn clean install spring-boot:run`