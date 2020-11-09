pipeline {
  agent any
  stages {
   stage('Build') {
      steps {
        withEnv(overrides: ["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'Maven'}/bin:${env.JAVA_HOME}/bin"]) {
          bat 'mvn -f apiops-anypoint-bdd-sapi/pom.xml clean install -DskipTests'
        }

      }
    }

    // Need to uncomment and check in remote Jenkins once nexus is installed and working there
    //stage('upload to nexus') {
      //steps {
        //script {
          //pom = readMavenPom file: "apiops-anypoint-bdd-sapi/pom.xml";

    /*stage('Munit') {
      steps {
        sh 'mvn -f apiops-anypoint-bdd-sapi/pom.xml test'
      }
    }

   //stage('Deploy') {
     // steps {
     //  withEnv(overrides: ["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'Maven'}/bin:${env.JAVA_HOME}/bin"]) {
      //    sh 'mvn -f apiops-anypoint-bdd-sapi/pom.xml package deploy -DmuleDeploy -Dtestfile=runner.TestRunner.java -Danypoint.username=joji4 -Danypoint.password=Canadavisa25@ -DapplicationName=apiops-bdd-sapi-jo -Dcloudhub.region=us-east-2'
     //  }


          //filesbyGlob = findFiles(glob: "target/*.jar");

          //nexusArtifactUploader(artifacts: [[artifactId: pom.artifactId, classifier: '', file: filesbyGlob[0].path, type: 'jar']], credentialsId: 'nexus', groupId: pom.groupId, nexusUrl: 'localhost:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'com.njclabs', version: pom.version)
        //}

      //}
    //}

   /* stage('Deploy') {
      steps {
        withEnv(overrides: ["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'Maven'}/bin:${env.JAVA_HOME}/bin"]) {
          sh 'mvn -f apiops-anypoint-bdd-sapi/pom.xml package deploy -DmuleDeploy -Dtestfile=runner.TestRunner.java -Danypoint.username=joji4 -Danypoint.password=Canadavisa25@ -DapplicationName=apiops-bdd-sapi-jo -Dcloudhub.region=us-east-2'
        }


      }
    }
    stage('SonarQube'){
            steps {
                withSonarQubeEnv('SonarQube') {
                   sh "mvn -f apiops-anypoint-bdd-sapi/pom.xml sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.sources=src/main/"
           
                }
            }
        }

        stage('Quality Gate'){
            steps {
                script {
                    timeout(time: 1, unit: 'HOURS') { 
                        sh "curl -u admin:admin -X GET -H 'Accept: application/json' http://localhost:9000/api/qualitygates/project_status?projectKey=com.mycompany:apiops-anypoint-bdd-sapi > status.json"
                        def json = readJSON file:'status.json'
                        echo "${json.projectStatus}"
                        if ("${json.projectStatus.status}" != "OK") {
                            currentBuild.result = 'FAILURE'
                            error('Pipeline aborted due to quality gate failure.')
                        }
                    }
                }
            }
        }*/
     stage('Build image') {
      steps {
        
        script {
          dockerImage= docker.build("njc/apiops-anypoint-bdd-sapi")
        }

        echo 'image built'
      }
    }

    stage('Run container') {
      steps {
        script {
          bat 'docker run -itd -p 8081:8081 --name apiops-anypoint-bdd-sapi  njc/apiops-anypoint-bdd-sapi'
        }

        echo 'container running'
      }
    }



    stage('FunctionalTesting') {
      steps {
        sleep(10)
        withEnv(overrides: ["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'Maven'}/bin:${env.JAVA_HOME}/bin"]) {
          bat 'mvn -f cucumber-API-Framework/pom.xml test'
        }

      }
    }

    stage('GenerateReports') {
      steps {
        cucumber(failedFeaturesNumber: -1, failedScenariosNumber: -1, failedStepsNumber: -1, fileIncludePattern: 'cucumber.json', jsonReportDirectory: 'cucumber-API-Framework/target', pendingStepsNumber: -1, skippedStepsNumber: -1, sortingMethod: 'ALPHABETICAL', undefinedStepsNumber: -1)
      }
    }

    stage('fetch properties') {
      steps {
        script {
          readProps= readProperties file: 'cucumber-API-Framework/src/main/resources/email.properties'
          echo "${readProps['email.to']}"
        }

      }
    }

    stage('Email') {
      steps {
        emailext(subject: 'Testing Reports for $PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', body: 'Please find the functional testing reports. In order to check the logs also, please go to url: $BUILD_URL'+readFile("cucumber-API-Framework/src/main/resources/emailTemplate.html"), attachmentsPattern: 'cucumber-API-Framework/target/cucumber-reports/report.html', from: "${readProps['email.from']}", mimeType: "${readProps['email.mimeType']}", to: "${readProps['email.to']}")
      }
    }
  
 stage('Kill container') {
      steps {
        script {
          bat 'docker stop apiops-anypoint-bdd-sapi'
          bat 'docker rm apiops-anypoint-bdd-sapi'
        }


        echo 'container Killed'
      }
    }


  
  }
  tools {
    maven 'Maven'
  }
  post {
    failure {
      emailext(subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', body: 'Please find attached logs.', attachLog: true, from: "${readProps['email.from']}", to: "${readProps['email.to']}")
    }

  }
}
