pipeline 
{
     agent 
     {
         docker
          {
            image 'maven:3-alpine'
            //This exposes application through port 8081 to outside world
            args '-v /root/.m2:/root/.m2'
         }
    } 
    stages 
     {
        stage('Build') 
         {
              steps 
              {
                sh 'mvn -B -DskipTests clean package'
              }
          }

        stage('Test') 
        {
            steps
            {
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
    } //End of stages

} //End of pipeline













/*
pipeline 
{
      agent 
        { 
            docker
             {
               image 'maven:3-alpine'
               args '-v /root/.m2:/root/.m2'
            }  
        }    
      
      
      environment
      {
            PATH= "/usr/local/bin:${PATH}"
      }
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
                      sh 'mvn -B -DskipTests clean package'
                         //sh 'mvn --version'
                         //sh 'sudo dseditgroup -o edit -a jenkins -t user docker'
                         //sh 'sudo docker ps'
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
                          sh 'uname -a'
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
                  when 
                    {
                      branch 'production'
                  }
                  steps 
                   {
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
            }//End of stages
       
 } //End of pipeline
    

*/






/* *******************************************************************************************************        */


/*
def dockerHome="/usr/local/"
def mavenHome="/usr/local/"
pipeline 
{
      agent 
        { 
            docker
             {
               image 'maven:3-alpine'
               args '-v /root/.m2:/root/.m2'
            }  
        } 

     environment 
          { 
              PATH = "${dockerHome}/bin:${mavenHome}/bin:${PATH}"
          }
     
     stages 
     {
         stage('Build') 
           {
               
      
              steps 
              {
                //sh 'mvn -B -DskipTests clean package'
                   sh 'uname -a'
                   sh 'mvn --version'
                   //sh 'sudo dseditgroup -o edit -a jenkins -t user docker'
                   sh 'sudo docker ps'
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

*/







/* *******************************************************************************************************        */





/*
pipeline 
{ 
 agent any 
  tools
  {
   def dockerHome = tool 'MyDocker'
   def mavenHome  = tool 'MyMaven'
  }
    stages 
          {
               stage('Initialize')
               {
                    steps 
                        {
                          //def dockerHome = '${params.MyDocker}'
                          //def mavenHome  = '${params.MyMaven}'
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
}
*/




/* *******************************************************************************************************        */



