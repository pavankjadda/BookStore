#!/usr/bin/env bash


echo 'The following Maven command installs your Maven-built Java application'
echo 'into the local Maven repository, which will ultimately be stored in'
echo 'Jenkins''s local Maven repository (and the "maven-repository" Docker data'
echo 'volume).'
set -x
mvn jar:jar install:install help:evaluate -Dexpression=project.name
set +x

echo 'The following complex command extracts the value of the <name/> element'
echo 'within <project/> of your Java/Maven project''s "pom.xml" file.'
set -x
NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`
NAME=${NAME,,}
set +x

echo 'The following complex command behaves similarly to the previous one but'
echo 'extracts the value of the <version/> element within <project/> instead.'
set -x
VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`
set +x

echo 'The following command runs and outputs the execution of your Java'
echo 'application (which Jenkins built using Maven) to the Jenkins UI.'
set -x
//ifconfig
//nohup java -jar target/${NAME}-${VERSION}.jar &
//java -jar target/${NAME}-${VERSION}.jar

 
echo 'Install docker'
apk update
apk add docker

echo 'Building docker image of the Application'
docker build -f Dockerfile -t duppoc/bookstore:latest .

echo 'Login into Dockerhub'
docker login -u duppoc -p Bcmc@2018

echo 'Pushing to docker hub'
docker push duppoc/bookstore:latest

echo 'Get docker image'
docker run  -d -p 8081:8081 duppoc/bookstore
