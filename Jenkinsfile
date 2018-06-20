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
        sh 'uname -a'
        sh 'mvn -B -DskipTests clean package'
      }

    stage('Test')
    {
        //sh 'mvn test'
        sh 'ifconfig'
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
    }
    catch(error)
    {

    }
}
