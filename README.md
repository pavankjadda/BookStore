# Jenkins CICD pipeline on OpenShift
Deploy Spring Boot Project with Jenkins CICD pipeline on OpenShift

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

# Build Application from source code/get from Artifact Repository
  This [article](https://access.redhat.com/documentation/en-us/red_hat_jboss_middleware_for_openshift/3/html-single/red_hat_java_s2i_for_openshift/index) explains
  the whole process. Here is a short version of it
## Source to Image (S2I) Build

To run and configure the Java S2I for OpenShift image, use the OpenShift S2I process.

The S2I process for the Java S2I for OpenShift image works as follows:

Log into the OpenShift instance by running the following command and providing credentials.

    $ oc login

Create a new project.

    $ oc new-project <project-name>

Create a new application using the Java S2I for OpenShift image. <source-location> can be the URL of a git repository or a path to a local folder.

    $ oc new-app redhat-openjdk18-openshift~<source-location>
Get the service name.

    $ oc get service

Expose the service as a route to be able to use it from the browser. <service-name> is the value of NAME field from previous command output.

    $ oc expose svc/<service-name> --port=8080

Get the route.

    $ oc get route

Access the application in your browser using the URL (value of HOST/PORT field from previous command output).

## Binary Builds (Build from Jar, War file)
### To deploy existing applications on OpenShift, you can use the binary source capability.

Prerequisite:

Get the JAR application archive or build the application locally.
The example below uses the undertow-servlet quickstart.

Clone the source code.

        $ git clone https://github.com/jboss-openshift/openshift-quickstarts.git

Configure the Red Hat JBoss Middleware Maven repository.

Build the application.

        $ cd openshift-quickstarts/undertow-servlet/

        $ mvn clean package
        [INFO] Scanning for projects...
        ...
        [INFO]
        [INFO] ------------------------------------------------------------------------
        [INFO] Building Undertow Servlet Example 1.0.0.Final
        [INFO] ------------------------------------------------------------------------
        ...
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 1.986 s
        [INFO] Finished at: 2017-06-27T16:43:07+02:00
        [INFO] Final Memory: 19M/281M
        [INFO] ------------------------------------------------------------------------

Prepare the directory structure on the local file system.

Application archives in the deployments/ subdirectory of the main binary build directory are copied directly to the standard deployments folder of the image being built on OpenShift. For the application to deploy, the directory hierarchy containing the web application data must be correctly structured.

Create main directory for the binary build on the local file system and deployments/ subdirectory within it. Copy the previously built JAR archive to the deployments/ subdirectory:

#### ignore above two steps as we already did this through Jenkins file
    $ ls
    dependency-reduced-pom.xml  pom.xml  README  src  target

    $ mkdir -p ocp/deployments

    $ cp target/undertow-servlet.jar ocp/deployments/

##### Note
Location of the standard deployments directory depends on the underlying base image, that was used to deploy the application. See the following table:

Perform the following steps to run application consisting of binary input on OpenShift:

Log into the OpenShift instance by running the following command and providing credentials.

    $ oc login

Create a new project.

    $ oc new-project <jdk-bin-demo>
(Optional) Identify the image stream for the particular image.

    $ oc get is -n openshift | grep ^redhat-openjdk | cut -f1 -d ' '
    redhat-openjdk18-openshift
Create new binary build, specifying image stream and application name.

    $ oc new-build --binary=true \
    --name=jdk-us-app \
    --image-stream=redhat-openjdk18-openshift:1.3
    --> Found image c1f5b31 (2 months old) in image stream "openshift/redhat-openjdk18-openshift" under tag "latest" for "redhat-openjdk18-openshift"

    Java Applications
    -----------------
    Platform for building and running plain Java applications (fat-jar and flat classpath)
    Tags: builder, java

    * A source build using binary input will be created
    * The resulting image will be pushed to image stream "jdk-us-app:latest"
    * A binary build was created, use 'start-build --from-dir' to trigger a new build

        --> Creating resources with label build=jdk-us-app ...
            imagestream "jdk-us-app" created
            buildconfig "jdk-us-app" created
        --> Success

Start the binary build. Instruct oc executable to use main directory of the binary build we created in previous step as the directory containing binary input for the OpenShift build.

    $ oc start-build jdk-us-app --from-dir=./ocp --follow
    Uploading directory "ocp" as binary input for the build ...
    build "jdk-us-app-1" started
    Receiving source from STDIN as archive ...
    ==================================================================
    Starting S2I Java Build .....
    S2I source build with plain binaries detected
    Copying binaries from /tmp/src/deployments to /deployments ...
    ... done
    Pushing image 172.30.197.203:5000/jdk-bin-demo/jdk-us-app:latest ...
    Pushed 0/6 layers, 2% complete
    Pushed 1/6 layers, 24% complete
    Pushed 2/6 layers, 36% complete
    Pushed 3/6 layers, 54% complete
    Pushed 4/6 layers, 71% complete
    Pushed 5/6 layers, 95% complete
    Pushed 6/6 layers, 100% complete
    Push successful

Create a new OpenShift application based on the build.

    $ oc new-app jdk-us-app
    --> Found image 66f4e0b (About a minute old) in image stream "jdk-bin-demo/jdk-us-app" under tag "latest" for "jdk-us-app"

        jdk-bin-demo/jdk-us-app-1:c1dbfb7a
        ----------------------------------
        Platform for building and running plain Java applications (fat-jar and flat classpath)

        Tags: builder, java

        * This image will be deployed in deployment config "jdk-us-app"
        * Ports 8080/tcp, 8443/tcp, 8778/tcp will be load balanced by service "jdk-us-app"
          * Other containers can access this service through the hostname "jdk-us-app"

    --> Creating resources ...
        deploymentconfig "jdk-us-app" created
        service "jdk-us-app" created
    --> Success
        Run 'oc status' to view your app.

Expose the service as route.

    $ oc get svc -o name
    service/jdk-us-app

    $ oc expose svc/jdk-us-app
    route "jdk-us-app" exposed

Access the application.

Access the application in your browser using the URL http://jdk-us-app-jdk-bin-demo.openshift.example.com
