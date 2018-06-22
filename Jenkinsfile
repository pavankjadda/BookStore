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
            //sh 'mvn test'
            sh 'ifconfig'
            sh 'uname -a'
        }

        stage('Deliver')
          {
                sh 'bash ./jenkins/deliver-openshift.sh'
                sh 'docker build -f Dockerfile -t 172.30.1.1:5000/cicd-project/bookstore:latest .'
                sh 'docker tag myimage 172.30.1.1:5000/cicd-project/bookstore:latest'

                //docker login -u duppoc -p Bcmc@2018
                sh 'oc login https://192.168.64.7:8443 --token=MpjL-m_ctMxNpphadQZO88GF7u3IwKDxC4Lb7p5xVv8'

                //docker push duppoc/bookstore:latest
                sh 'sudo docker push 172.30.1.1:5000/cicd-project/bookstore:latest'
        }
}

//ignore this method, not using it
def imagePrune(containerName)
{
    try {
        sh "docker image prune -f"
        sh "docker stop $containerName"
    } catch(error){}
}
