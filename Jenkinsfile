pipeline {
     agent any
     /*
     {
        docker {
            image 'maven:3-alpine'
            //This exposes application through port 8081 to outside world
            args '-p 8081:8081'
        }
    } */
    stages {
        stage('Build') {
            steps {
                 sh 'ifconfig'
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                //sh 'mvn test'
                sh 'echo "test"'
            }
            post {
                always {
                    //junit 'target/surefire-reports/*.xml'
                    sh 'echo "test"'
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

        stage('Deliver') {
        when {
              branch 'production'
           }
            steps {
                sh 'bash ./jenkins/deliver.sh'
            }
        }
    }

}
