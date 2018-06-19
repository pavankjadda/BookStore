# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

# copy application WAR (with libraries inside)
COPY target/bookstore-0.0.1-SNAPSHOT.jar /bookstore.jar

# specify default command
CMD ["/usr/bin/java", "-jar", "/bookstore.jar"]

RUN apk add maven

RUN apk add docker

EXPOSE 8081:8081
