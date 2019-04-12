# Spring Boot Project deployed with Jenkins pipeline on OpenShift 
Deploy Spring Boot Project with Jenkins CICD pipeline on OpenShift

## Introduction

On every pipeline execution, the code goes through the following steps:

1. Code is cloned from **Github** or Gogs, built, tested and analyzed for bugs and bad patterns
2. The JAR artifact is pushed to **Nexus** Repository manager
3. A container image (_bookstore:latest_) is built based on the _bookstore_ application JAR artifact
4. The _bookstore_ container image is deployed in a fresh new container in bookstore_dev project
5. If tests successful, the **bookstore_dev** image is tagged with the application version in the **bookstore_stage** project
6. The staged image is deployed in a fresh new container in the **bookstore_stage** project

The following diagram shows the steps included in the deployment pipeline:

![](src/main/resources/pipeline.png)

The application used in this pipeline is a Spring Boot application which is available on **src** folder in this repository 

## Prerequisites
* 8+ GB memory
* redhat-openjdk18-openshift imagestreams imported to OpenShift (see Troubleshooting section for details)

### Start up an OpenShift cluster:

```
minishift addons enable xpaas
#Adjust memory and cpus based on your PC, macbook or Ubuntu configuration
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

### Get OC environment value and add it shell
##### Execute the following command and follow instructions on screen for next steps
```
$ minishift oc-env
 <Execute output of this command to get access to oc environment>

```

## Automated Deploy on OpenShift (Not Recommended)
You can se the `scripts/provision.sh` script provided to deploy the entire demo:

  ```
  ./provision.sh --help
  ./provision.sh deploy --deploy-che --ephemeral
  ./provision.sh delete
  ```

## Manual Deploy on OpenShift
Create the following projects for CI/CD components, Dev and Stage environments:

  ```
  # Create Projects
  oc new-project bookstore-dev --display-name="Bookstore - Dev"
  oc new-project bookstore-stage --display-name="Bookstore - Stage"
  oc new-project cicd --display-name="cicd"

  # Grant Jenkins Access to Projects
  oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n bookstore-dev
  oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n bookstore-stage

  ```  
  if add-role-to-user step fails execute following step and repeat last step again
  ```
  oc login -u system:admin
  oc adm policy add-cluster-role-to-user cluster-admin <username>
  ```
  
Clone the the project and navigate to the folder
```
  git clone https://github.com/pavankjadda/BookStore.git --branch=<branch name>
  cd Bookstore
```

Start Jenkins Persistent pod using following command

  ```
  oc new-app -n templates/jenkins-persistsnet-template.json
  ```
Start SonarQube with Postgres database using following command

```
oc new-app -f templates/sonarqube-postgresql-template.yaml --param=SONARQUBE_VERSION=6.7
```

Start nexus artifact repository with persistent storage
```
oc new-app -f templates/nexus3-persistent-template.yaml --param=NEXUS_VERSION=3.15.2
```

# Jenkinsfile
Following Jenkinsfile (this code located inside cicd-template.yaml file) contains steps to automate the build and deployment process. Please make changes to Jenkinsfile if you want to add/remove steps in future. Next step explains the same process in a manual way. 
def version, mvnCmd = "mvn -s templates/cicd-settings-nexus3.xml"
      pipeline
      {
       agent any
        tools
        {
            maven 'M3'
        }

        stages
        {
          stage('Build App')
          {
            steps
             {
              git branch: 'openshift-aws', url: 'https://github.com/pavankjadda/BookStore.git'
              script {
                  def pom = readMavenPom file: 'pom.xml'
                  version = pom.version
              }
              sh "mvn install -DskipTests=true"
            }
          }
          stage('Test')
          {
            steps
            {
                  echo "Test Stage"
              sh "${mvnCmd} test -Dspring.profiles.active=test"
              //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
            }
          }
          stage('Code Analysis')
          {
            steps
             {
              script
              {
                      sh "${mvnCmd} sonar:sonar -Dsonar.host.url=http://sonarqube:9000  -DskipTests=true"
              }
            }
          }
          /*
          stage('Archive App') {
            steps {
              sh "${mvnCmd} deploy -DskipTests=true -P nexus3"
            }
          }*/

          stage('Create Image Builder') {

            when {
              expression {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    return !openshift.selector("bc", "bookstore").exists()
                  }
                }
              }
            }
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    openshift.newBuild("--name=bookstore", "--image-stream=redhat-openjdk18-openshift:latest", "--binary=true")
                  }
                }
              }
            }
          }
          stage('Build Image') {
            steps {
              sh "rm -rf ocp && mkdir -p ocp/deployments"
              sh "pwd && ls -la target "
              sh "cp target/bookstore-*.jar ocp/deployments"

              script {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    openshift.selector("bc", "bookstore").startBuild("--from-dir=./ocp","--follow", "--wait=true")
                  }
                }
              }
            }
          }
          stage('Create DEV') {
            when {
              expression {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    return !openshift.selector('dc', 'bookstore').exists()
                  }
                }
              }
            }
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    def app = openshift.newApp("bookstore:latest")
                    app.narrow("svc").expose()

                    //http://localhost:8080/actuator/health
                    openshift.set("probe dc/bookstore --readiness --get-url=http://:8080/actuator/health --initial-delay-seconds=30 --failure-threshold=10 --period-seconds=10")
                    openshift.set("probe dc/bookstore --liveness  --get-url=http://:8080/actuator/health --initial-delay-seconds=180 --failure-threshold=10 --period-seconds=10")

                    def dc = openshift.selector("dc", "bookstore")
                    while (dc.object().spec.replicas != dc.object().status.availableReplicas) {
                        sleep 10
                    }
                    openshift.set("triggers", "dc/bookstore", "--manual")
                  }
                }
              }
            }
          }
          stage('Deploy DEV') {
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject(env.DEV_PROJECT) {
                    openshift.selector("dc", "bookstore").rollout().latest()
                  }
                }
              }
            }
          }
          stage('Promote to STAGE?') {
            steps {
              script {
                openshift.withCluster() {
                  openshift.tag("${env.DEV_PROJECT}/bookstore:latest", "${env.STAGE_PROJECT}/bookstore:${version}")
                }
              }
            }
          }
          stage('Deploy STAGE') {
            steps {
              script {
                openshift.withCluster() {
                  openshift.withProject(env.STAGE_PROJECT) {
                    if (openshift.selector('dc', 'bookstore').exists()) {
                      openshift.selector('dc', 'bookstore').delete()
                      openshift.selector('svc', 'bookstore').delete()
                      openshift.selector('route', 'bookstore').delete()
                    }

                    openshift.newApp("bookstore:${version}").narrow("svc").expose()
                    openshift.set("probe dc/bookstore --readiness --get-url=http://:8080/actuator/health --initial-delay-seconds=30 --failure-threshold=10 --period-seconds=10")
                    openshift.set("probe dc/bookstore --liveness  --get-url=http://:8080/actuator/health --initial-delay-seconds=180 --failure-threshold=10 --period-seconds=10")
                  }
                }
              }
            }
          }
        }
      }

---




# Ignore this step (Explains the backend process) 

Jenkinsfile has same code but this step explains the steps one by one. Wrote this only for understanding purpose. Build Application from source code/get from Artifact Repository. This [article](https://access.redhat.com/documentation/en-us/red_hat_jboss_middleware_for_openshift/3/html-single/red_hat_java_s2i_for_openshift/index) explains
  the whole process. Here is a short version of it

## Source to Image (S2I) Build (Not recommended)

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

    $ oc new-project <bookstore>
(Optional) Identify the image stream for the particular image.

    $ oc get is -n openshift | grep ^redhat-openjdk | cut -f1 -d ' '
    redhat-openjdk18-openshift
Create new binary build, specifying image stream and application name.

    $ oc new-build --binary=true \
    --name=bookstore \
    --image-stream=redhat-openjdk18-openshift:1.3
    --> Found image c1f5b31 (2 months old) in image stream "openshift/redhat-openjdk18-openshift" under tag "latest" for "redhat-openjdk18-openshift"

    Java Applications
    -----------------
    Platform for building and running plain Java applications (fat-jar and flat classpath)
    Tags: builder, java

    * A source build using binary input will be created
    * The resulting image will be pushed to image stream "bookstore:latest"
    * A binary build was created, use 'start-build --from-dir' to trigger a new build

        --> Creating resources with label build=jdk-us-app ...
            imagestream "bookstore" created
            buildconfig "bookstore" created
        --> Success

Start the binary build. Instruct oc executable to use main directory of the binary build we created in previous step as the directory containing binary input for the OpenShift build.

    $ oc start-build bookstore --from-dir=./ocp --follow
    Uploading directory "ocp" as binary input for the build ...
    build "bookstore-1" started
    Receiving source from STDIN as archive ...
    ==================================================================
    Starting S2I Java Build .....
    S2I source build with plain binaries detected
    Copying binaries from /tmp/src/deployments to /deployments ...
    ... done
    Pushing image 172.30.197.203:5000/bookstore/bookstore:latest ...
    Pushed 0/6 layers, 2% complete
    Pushed 1/6 layers, 24% complete
    Pushed 2/6 layers, 36% complete
    Pushed 3/6 layers, 54% complete
    Pushed 4/6 layers, 71% complete
    Pushed 5/6 layers, 95% complete
    Pushed 6/6 layers, 100% complete
    Push successful

Create a new OpenShift application based on the build.

    $ oc new-app bookstore
    --> Found image 66f4e0b (About a minute old) in image stream "bookstore/bookstore" under tag "latest" for "bookstore"

        bookstore/bookstore-1:c1dbfb7a
        ----------------------------------
        Platform for building and running plain Java applications (fat-jar and flat classpath)

        Tags: builder, java

        * This image will be deployed in deployment config "jdk-us-app"
        * Ports 8080/tcp, 8443/tcp, 8778/tcp will be load balanced by service "jdk-us-app"
          * Other containers can access this service through the hostname "jdk-us-app"

    --> Creating resources ...
        deploymentconfig "bookstore" created
        service "bookstore" created
    --> Success
        Run 'oc status' to view your app.

Expose the service as route.

    $ oc get svc -o name
    service/bookstore

    $ oc expose svc/bookstore
    route "bookstore" exposed

Access the application.

Access the application in your browser using the URL http://bookstore-bookstore-dev.192.168.99.100.nip.io/books.html#!/
