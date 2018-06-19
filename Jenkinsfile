
pipeline 
{ 
 agent any 
  
    stages 
          {
               stage('Initialize')
               {
                    steps 
                        {
                          def dockerHome = tool 'MyDocker'
                          def mavenHome  = tool 'MyMaven'
                          //env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
                        }

               }

              stage('Build') 
              {
                   steps 
                   {
                     //sh 'mvn -B -DskipTests clean package'
                     sh 'uname -a'
                        sh 'mvn'
                        sh 'docker ps'
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

                 steps 
                 {
                     sh 'bash ./jenkins/deliver.sh'
                 }
             }
           } //End of stages
}



/* *******************************************************************************************************        */
/*
node {

    stage('Initialize')
     {
        def dockerHome = tool 'MyDocker'
        def mavenHome  = tool 'MyMaven'
        env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
    }

      stage('Build') 
      {
           steps 
           {
             //sh 'mvn -B -DskipTests clean package'
             sh 'uname -a'
                sh 'mvn'
                sh 'docker ps'
           }
       }

     stage('Test') 
     {
         steps 
         {
             //sh 'mvn test'
             sh 'ifconfig'
         }
         post 
         {
             always 
             {
                 //junit 'target/surefire-reports/*.xml'
                 sh 'uname -a'
                  //sh 'apk add docker'
                  //sh 'service docker start'
             }
         }
     }

     stage('Deliver for development')
     {
         when 
         {
             branch 'development'
         }
         steps 
         {
             sh './jenkins/scripts/deliver-for-development.sh'
             input message: 'Finished using the web site? (Click "Proceed" to continue)'
         }
     }

     stage('Deploy for production')
     {
         deployForProduction();
         
     }

     stage('Deliver') 
       {

         steps 
         {
             sh 'bash ./jenkins/deliver.sh'
         }
     }
      
}

def deployForProduction()
{
    when {
             branch 'production'
         }
         steps 
        {
             sh './jenkins/scripts/deploy-for-production.sh'
             input message: 'Finished using the web site? (Click "Proceed" to continue)'
         }
}
*/


/* *******************************************************************************************************        */

/*

pipeline 
{
     //agent any
     

     agent 
     {
          node
          {
               label 'my-defined-label'
               stages 
               {
                    stage('Initialize')
                    {
                         steps 
                             {
                               def dockerHome = tool 'MyDocker'
                               def mavenHome  = tool 'MyMaven'
                               env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
                             }

                    }
               } //End of Stages
          } //End of node
     } //End of Agent
     
          stages 
          {
               stage('Initialize')
               {
                    steps 
                        {
                          def dockerHome = tool 'MyDocker'
                          def mavenHome  = tool 'MyMaven'
                          env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
                        }

               }

              stage('Build') 
              {
                   steps 
                   {
                     //sh 'mvn -B -DskipTests clean package'
                     sh 'uname -a'
                        sh 'mvn'
                        sh 'docker ps'
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

                 steps 
                 {
                     sh 'bash ./jenkins/deliver.sh'
                 }
             }
           } //End of stages
     
      
         docker
          {
            image 'maven:3-alpine'
            //This exposes application through port 8081 to outside world
            args '-u root -p 8081:8081 -v /var/run/docker.sock:/var/run/docker.sock  -v jenkins-data:/var/jenkins_home '
         } 
 
}*/
