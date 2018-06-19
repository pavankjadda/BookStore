# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

USER root
# copy application WAR (with libraries inside)
COPY target/bookstore-0.0.1-SNAPSHOT.jar /bookstore.jar

# specify default command
CMD ["/usr/bin/java", "-jar", "/bookstore.jar"]

RUN apk update && apk add docker  && apk add maven
RUN apk update

EXPOSE 8081:8081
