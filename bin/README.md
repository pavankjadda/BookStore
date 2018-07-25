# BookStore
Spring Boot Book Store project demonstrates CICD pipeline with Jenkins

## Introduction

On every pipeline execution, the code goes through the following steps:

1. Code is cloned from Gogs, built, tested and analyzed for bugs and bad patterns
2. The WAR artifact is pushed to Nexus Repository manager
3. A container image (_tasks:latest_) is built based on the _Tasks_ application WAR artifact deployed on JBoss EAP 6
4. The _Tasks_ container image is deployed in a fresh new container in DEV project
5. If tests successful, the DEV image is tagged with the application version (_tasks:7.x_) in the STAGE project
6. The staged image is deployed in a fresh new container in the STAGE project

The following diagram shows the steps included in the deployment pipeline:

![](images/pipeline.png?raw=true)

The application used in this pipeline is a JAX-RS application which is available on GitHub and is imported into Gogs during the setup process:
[https://github.com/OpenShiftDemos/openshift-tasks](https://github.com/OpenShiftDemos/openshift-tasks/tree/eap-7)

## Prerequisites
* 10+ GB memory
* JBoss EAP 7 imagestreams imported to OpenShift (see Troubleshooting section for details)

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

## Automated Deploy on OpenShift
You can se the `scripts/provision.sh` script provided to deploy the entire demo:

  ```
  ./provision.sh --help
  ./provision.sh deploy --deploy-che --ephemeral
  ./provision.sh delete 
  ```
  
## Manual Deploy on OpenShift
Follow these [instructions](docs/local-cluster.md) in order to create a local OpenShift cluster. Otherwise using your current OpenShift cluster, create the following projects for CI/CD components, Dev and Stage environments:

  ```shell
  # Create Projects
  oc new-project dev --display-name="Tasks - Dev"
  oc new-project stage --display-name="Tasks - Stage"
  oc new-project cicd --display-name="CI/CD"

  # Grant Jenkins Access to Projects
  oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n dev
  oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n stage
  ```  

And then deploy the demo:

  ```
  # Deploy Demo
  oc new-app -n cicd -f cicd-template.yaml

  # Deploy Demo woth Eclipse Che
  oc new-app -n cicd -f cicd-template.yaml --param=WITH_CHE=true
  ```

To use custom project names, change `cicd`, `dev` and `stage` in the above commands to
your own names and use the following to create the demo:

  ```shell
  oc new-app -n cicd -f cicd-template.yaml --param DEV_PROJECT=dev-project-name --param STAGE_PROJECT=stage-project-name
  ```

