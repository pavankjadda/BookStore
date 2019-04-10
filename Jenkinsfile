def templateName = 'postgres'

pipeline {

  agent {
    label 'maven'
  }

  parameters
          {
            string(name: 'DEV_PROJECT', defaultValue: 'sandbox', description: 'Sandbox Project')
            string(name: 'POSTGRESQL_USER', defaultValue: 'admin', description: 'Postgres User')
            string(name: 'POSTGRESQL_PASSWORD', defaultValue: 'Gosox07!', description: 'Postgres Password')
            string(name: 'POSTGRESQL_DATABASE', defaultValue: 'mockdb', description: 'Postgres DB')
            string(name: 'DEV_API_ROUTE', defaultValue: 'elasticsearch-cicd.apps.bcmcgroup.com', description: 'Dev Rest API Route')
          }

  stages {

    stage('Build App') {
      steps {
        sh "mvn clean install -DskipTests=true"

      }
    }
    stage('Create Image Builder - REST') {
      when {
        expression
                {
                  openshift.withCluster()
                          {
                            return !openshift.selector("bc", "mock-rest").exists()
                          }
                }
      }
      steps {
        script {
          openshift.withCluster()
                  {
                    openshift.newBuild("--name=mock-rest", "--image-stream=cicd/redhat-openjdk18-openshift:latest", "--binary")
                  }
        }
      }
    }
    stage('Build Image - Rest') {
      steps {
        sh "rm -rf ocp-rest && mkdir -p ocp-rest/deployments"
        sh "pwd && ls -la target "
        sh "cp target/bookstore-*.jar ocp-rest/deployments"
        script
                {
                  openshift.withCluster()
                          {
                            openshift.selector("bc", "mock-rest").startBuild("--from-dir=./ocp-rest", "--follow", "--wait=true")
                          }
                }
      }
    }


    /****************************** Development ************************************ */

    //Create Postgres DB on dev environment
    stage('Cleanup dev postgres db') {
      steps {
        script {
          openshift.withCluster() {
            openshift.withProject(params.DEV_PROJECT) {
              openshift.selector("all", [template: templateName]).delete()
              if (openshift.selector("secrets", templateName).exists()) {
                openshift.selector("secrets", templateName).delete()
              }
            }
          }
        }
      }
    }
    stage('Create dev postgres db')
            {
              when {
                expression {
                  openshift.withCluster()
                          {
                            openshift.withProject(params.DEV_PROJECT)
                                    {
                                      return !openshift.selector("dc", "postgresdb").exists()
                                    }
                          }
                }
              }
              steps {
                script {
                  openshift.withCluster()
                          {
                            openshift.withProject(params.DEV_PROJECT)
                                    {
                                      openshift.newApp('postgresql', '--name=postgresdb', 'POSTGRESQL_USER=' + params.POSTGRESQL_USER, 'POSTGRESQL_DATABASE=' + params.POSTGRESQL_DATABASE, 'POSTGRESQL_PASSWORD=' + params.POSTGRESQL_PASSWORD)
                                    }
                          }
                }
              }
            }


    //Create MockDb Dev REST DB project
    stage('Promote from Build to Dev')
            {
              steps {
                script
                        {
                          openshift.withCluster()
                                  {
                                    openshift.tag("cicd/mock-rest:latest", "${params.DEV_PROJECT}/mock-rest:latest")
                                  }
                        }
              }
            }


    stage('Create DEV - REST')
            {
              when {
                expression {
                  openshift.withCluster()
                          {
                            openshift.withProject(params.DEV_PROJECT)
                                    {
                                      return !openshift.selector('dc', 'mock-rest-dev').exists()
                                    }
                          }
                }
              }
              steps {
                script {
                  openshift.withCluster()
                          {
                            openshift.withProject(params.DEV_PROJECT)
                                    {
                                      if (openshift.selector('dc', 'mock-rest-dev').exists()) {
                                        openshift.selector('dc', 'mock-rest-dev').delete()
                                      }
                                      if (openshift.selector('svc', 'mock-rest-dev').exists()) {
                                        openshift.selector('svc', 'mock-rest-dev').delete()
                                      }
                                      if (openshift.selector('route', 'mock-rest-dev').exists()) {
                                        openshift.selector('route', 'mock-rest-dev').delete()
                                      }


                                      openshift.newApp("${params.DEV_PROJECT}/mock-rest:latest", "--name=mock-rest-dev").narrow('svc').expose("--hostname=${params.DEV_API_ROUTE}")
                                      openshift.set("probe dc/mock-rest-dev --readiness --get-url=http://:8080/actuator/health --initial-delay-seconds=30 --failure-threshold=10 --period-seconds=10")
                                      openshift.set("probe dc/mock-rest-dev --liveness  --get-url=http://:8080/actuator/health --initial-delay-seconds=180 --failure-threshold=10 --period-seconds=10")

                                      def dc = openshift.selector("dc", "mock-rest-dev")
                                      while (dc.object().spec.replicas != dc.object().status.availableReplicas) {
                                        sleep 10
                                      }
                                      openshift.set("triggers", "dc/mock-rest-dev", "--manual")
                                    }
                          }
                }
              }
            }


    stage('Deploy DEV - REST')
            {
              steps
                      {
                        script
                                {
                                  openshift.withCluster()
                                          {
                                            openshift.withProject(params.DEV_PROJECT)
                                                    {
                                                      openshift.selector("dc", "mock-rest-dev").rollout().latest()
                                                    }
                                          }
                                }
                      }
            }

  }
}

