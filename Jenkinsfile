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
              
                sh 'sudo docker build -f Dockerfile -t 172.30.199.177:5000/bookstore/bookstore:latest .'
                sh 'sudo docker tag 337b25f8b193 172.30.199.177:5000/bookstore/bookstore:latest'

                //docker login -u duppoc -p Bcmc@2018
                sh 'sudo docker login -u system -p Kf3rCGet34BeznzHSMET1q5py-5n68lY5Bvt03LmUz0 172.30.199.177:5000'

                //docker push duppoc/bookstore:latest
                sh 'sudo docker push 172.30.199.177]:5000/bookstore/bookstore:latest'
                
              /*
              docker.withRegistry('172.30.199.177:5000') 
              {
                def customImage = docker.build("bookstore:${env.BUILD_ID}")
                customImage.push()
                customImage.push('latest')
              } */
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
