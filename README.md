# Spring Boot Google Vision API Demo

## Description

Simple web app that processes image and identifies its content. App takes as input image file and outputs new image file with drawn overlay bounding boxes over recognized objects.

## How to run

### Before running app add google vision api credentials

**Important! don't commit hardcoded API key value. Below example just for dev enviroment testing and etc.**

Replace ${API_KEY} with your own generated google vision api key in `/src/main/resources/application.properties/spring.cloud.gcp.credentials.encoded-key=${API_KEY}`

There are several ways to run this application. You can run it from the command line with Maven, Docker or using Executable Jar.

Image upload has **3MB** file size limit. You can change limit in `/src/main/resources/application.properties`

Once the app starts, go to the web browser and visit `http://localhost:8080`

#### Please remember all provided commands need to be executed in projects root directory.

First time app startup could take longer. Because app uses [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin) and installs nodejs locally with other dependencies.

### Maven

#### Using the Maven Plugin

The Spring Boot Maven plugin includes a run goal that can be used to quickly compile and run your application.

```bash
$ mvn spring-boot:run
``` 

#### Using Executable Jar

To create an executable jar run:

```bash
$ mvn clean package
``` 

To run that application, use the java -jar command, as follows:

```bash
$ java -jar target/demo-0.0.1-SNAPSHOT.jar
```

To exit the application, press **ctrl-c**.

### Docker

It is possible to run app using Docker:

Build Docker image:
```bash
$ mvn clean package
```
```bash
$ docker build -t myapp/vision .
```

Run Docker container:
```bash
$ docker run --publish 8080:8090 myapp/vision
```

## Helper Tools
### H2 Database web interface

Go to the web browser and visit `http://localhost:8080/h2`

In field **JDBC URL** type:
```
jdbc:h2:file:./db/test;DB_CLOSE_ON_EXIT=FALSE
```
In field **User Name** type:
```
sa
```

In `/src/main/resources/application.properties` file it is possible to change both
web interface url path, as well as the datasource url.
