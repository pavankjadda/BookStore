pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'whoami'
                //sh '/usr/local/bin/docker ps'
                sh '/usr/local/bin/mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh '/usr/local/bin/mvn test'
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
