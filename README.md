# BookStore
Spring Boot Book Store project demonstrates CICD pipeline with Jenkins

### Start up an OpenShift cluster:

```
minishift addons enable xpaas
minishift start --memory=10240 --cpus=4 --vm-driver=virtualbox
oc login -u developer
```

### Pre-pull the images to make sure the deployments go faster:

```
minishift ssh docker pull openshiftdemos/gogs:0.11.34
minishift ssh docker pull registry.centos.org/rhsyseng/sonarqube:latest
minishift ssh docker pull sonatype/nexus3:3.8.0
minishift ssh docker pull registry.access.redhat.com/openshift3/jenkins-2-rhel7
minishift ssh docker pull registry.access.redhat.com/openshift3/jenkins-slave-maven-rhel7
minishift ssh docker pull registry.access.redhat.com/jboss-eap-7/eap70-openshift
```

### Add cluster-admin role to users
##### Execute the following command and follow instructions on screen for next steps
```
$ minishift oc-env
 <Execute output of this command to get access to oc environment>

$ oc login -u system:admin
$ oc adm policy add-cluster-role-to-user cluster-admin <username>
```
