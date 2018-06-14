pipeline {
     agent {
        docker {
<<<<<<< HEAD
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
=======
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
>>>>>>> 62c37c16c040a9ad03809dbd097c09ec6be273e6
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'whoami'
                //sh '/usr/local/bin/docker ps'
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') {
            steps {
                sh './jenkins/scripts/deliver.sh'
            }
        }
    }
}