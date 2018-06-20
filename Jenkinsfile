node
{
    stage('Initialize')
    {
        def dockerHome = tool 'MyDocker'
        def mavenHome  = tool 'MyMaven'
        env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
    }

    stage('Checkout')
    {
        checkout scm
    }


      stage('Build')
           {

            sh 'mvn -B -DskipTests clean package'
          }

        stage('Test')
        {
            sh 'mvn test'
            sh 'ifconfig'
            sh 'uname -a'
        }

        stage('Deliver')
          {
                sh 'bash ./jenkins/deliver-openshift.sh'
        }
}

def imagePrune(containerName)
{
    try {
        sh "docker image prune -f"
        sh "docker stop $containerName"
    } catch(error){}
}
