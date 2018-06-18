pipeline 
{
     agent any
     /*
     agent 
     {
         docker
          {
            image 'maven:3-alpine'
            //This exposes application through port 8081 to outside world
            args '-u root -p 8081:8081 -v /var/run/docker.sock:/var/run/docker.sock  -v jenkins-data:/var/jenkins_home '
         }
    } */
    stages 
     {
        stage('Build') 
         {
              steps 
              {
                //sh 'mvn -B -DskipTests clean package'
                   sh 'pwd'
              }
          }

        stage('Test') 
        {
            steps {
                //sh 'mvn test'
                sh 'ifconfig'
            }
            post {
                always {
                    //junit 'target/surefire-reports/*.xml'
                    sh 'uname -a'
                     //sh 'apk add docker'
                     //sh 'service docker start'
                }
            }
        }

        stage('Deliver for development')
        {
                    when {
                        branch 'development'
                    }
                    steps {
                        sh './jenkins/scripts/deliver-for-development.sh'
                        input message: 'Finished using the web site? (Click "Proceed" to continue)'
                    }
        }

        stage('Deploy for production')
        {
            when {
                branch 'production'
            }
            steps {
                sh './jenkins/scripts/deploy-for-production.sh'
                input message: 'Finished using the web site? (Click "Proceed" to continue)'
            }
        }

        stage('Deliver') 
          {
   
            steps {
                sh 'bash ./jenkins/deliver.sh'
            }
        }
    }

}
