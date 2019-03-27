
FROM openshift/base-centos7

# Builder version
ENV BUILDER_VERSION 1.0

# Install required util packages.
RUN yum -y update; \
    yum install tar -y; \
    yum install unzip -y; \
    yum install ca-certificates -y; \
    yum install sudo -y; \
    yum clean all -y


# Install OpenJDK 1.8, create required directories.
RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
    yum clean all -y && \
    mkdir -p /opt/openshift

COPY target/bookstore-*.jar  /opt/openshift
ENTRYPOINT ["/usr/lib/jvm/java-1.8.0-openjdk"]
CMD ["-jar", "/opt/openshift/bookstore-*.jar"]


# This default user is created in the openshift/base-centos7 image
USER 1001

# Set the default port for applications built using this image
EXPOSE 8080
