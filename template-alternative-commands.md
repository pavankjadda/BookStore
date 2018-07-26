
# Make sure increase timeouts for all deployments to 10 minutes or more

#This will start Jenkins, sometimes other applications too
1. oc new-app -n <project name> -f cicd-template.yaml

2. Use Github instead of gogs. Skip step 3 if you use Github or gitlab
3. oc new-app -f templates/gogs-template.yaml --param=GOGS_VERSION=0.11.34   --param=HOSTNAME='gogs'  --param=SKIP_TLS_VERIFY=true
  # if gogs fail in this step, install gogs and postgres db in 2 steps. Do this only if Gogs failed in above step
      a. Go to homepage and Add to Project --> deploy image --> look for image ' centos/postgresql-95-centos7 ' --> deploy. Make
         sure to configure the following values before deploying
            POSTGRESQL_USER = gogs
            POSTGRESQL_DATABASE = gogs
            POSTGRESQL_DATABASE = gogs
      b.Finish steps 3 and 4 before performing this as previous step takes time. Go to homepage and Add to Project --> deploy image --> look for image ' openshiftdemos/gogs ' --> deploy. Once
        done go to home page, click on route. In new window, enter host ip as pod ip (get it from applications --> pods --> postgres pod --> IP)
        Enter username and password as 'gogs'. This will connect gogs to Postgres DB started in previous step.

# Start sonarqube
4. oc new-app -f templates/sonarqube-template.yaml --param=SONARQUBE_VERSION=7.0 --param=SONAR_MAX_MEMORY=2Gi

#Start nexus artifact repository, this may take a while
5. oc new-app -f templates/nexus3-template.yaml --param=NEXUS_VERSION=3.7.1 --param=MAX_MEMORY=2Gi
