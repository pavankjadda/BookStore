pipeline {
     agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
            args '-p 3000:3000 -p 5000:5000'
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
        stage('Deliver') {
            steps {
                sh 'bash ./jenkins/deliver.sh'
            }
        }
    }
}
