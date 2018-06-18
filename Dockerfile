# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

# copy application WAR (with libraries inside)
COPY target/bookstore-*.war /bookstore.war
COPY target/BookStore-*.war /bookstore.war

# specify default command
CMD ["/usr/bin/java", "-jar", "/bookstore.war"]
