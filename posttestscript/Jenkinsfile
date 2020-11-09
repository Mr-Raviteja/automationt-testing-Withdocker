pipeline {
  agent any
  stages {
    stage('Start') {
      steps {
        echo 'Starting the pipeline'
      }
    }

    stage('Build') {
      steps {
        sh 'mvn -f pom.xml clean install'
      }
    }

    stage('Deploy') {
      steps {
        sh 'mvn -f pom.xml package deploy -DmuleDeploy'
      }
    }

    stage('IntegerationTesting') {
      steps {
        sh 'mvn -f pom.xml failsafe:integration-test'
      }
    }

    stage('GenerateReports') {
      steps {
        cucumber(fileIncludePattern: '**/cucumber.json', jsonReportDirectory: 'target')
      }
    }

    stage('Email') {
      steps {
        emailext(subject: 'Cucumber Reports', body: 'Attached the body', attachLog: true, attachmentsPattern: 'target/cucumber-reports/report.html', from: 'vikaspuri123@gmail.com', mimeType: 'text/html', to: 'vikas_mullana@yahoo.com')
      }
    }

  }
  tools {
    maven 'MAVEN_HOME'
  }
}