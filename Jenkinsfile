pipeline {
  agent any
  stages {
    stage("Slack Notification"){
      steps {
        slackSend color: '#2c3e50', message: "Started build <${BUILD_URL}|#${BUILD_NUMBER}> for ${JOB_NAME} (<https://git.ded1.denv.it/dvitali/tsp-cup-2019/commit/${GIT_COMMIT}|${GIT_COMMIT}>) on branch $GIT_BRANCH."
      }
    }

    stage("Cleanup"){
      steps {
        sh "git clean -fdx"
      }
    }

    stage('Checkout') {
        steps {
            checkout scm
        }
    }

    stage("Test"){
      steps {
        script {
          docker.image('maven:3-jdk-11').inside() {
            sh "mvn test"
            junit 'target/surefire-reports/**/*.xml'
          }
        }
      }
    }
    stage("Package"){
      steps {
        script {
          docker.image('maven:3-jdk-11').inside() {
            sh "mvn assembly:single"
          }
        }
      }
    }
  }
  post {
    failure {
        slackSend color: 'danger', message: "Build <${BUILD_URL}|#${BUILD_NUMBER}> *failed*! Branch $GIT_BRANCH, commit: (<https://git.ded1.denv.it/dvitali/tsp-cup-2019/commit/${GIT_COMMIT}|${GIT_COMMIT}>). :respects:\n\n*Commit Log*:\n${COMMIT_LOG}"
    }
    success {
        slackSend color: 'good', message: "Build <${BUILD_URL}|#${BUILD_NUMBER}> *successful*! Branch $GIT_BRANCH, commit: (<https://git.ded1.denv.it/dvitali/tsp-cup-2019/commit/${GIT_COMMIT}|${GIT_COMMIT}>). :tada: :blobdance: :clappa:\n\n*Commit Log*:\n${COMMIT_LOG}\n<https://artifacts.ded1.denv.it/${ENCODED_JOB_NAME}/${BUILD_NUMBER}.html|GIT Inspector Result>"
    }
    always {
        script {
          try {
            COMMIT_LOG = sh(script:"git log --oneline --pretty=format:'%h - %s (%an)' ${GIT_PREVIOUS_COMMIT}..HEAD", returnStdout: true)
          } catch(e){
            COMMIT_LOG = ""
          }
          ENCODED_JOB_NAME = URLEncoder.encode(JOB_NAME, "UTF-8")
        }
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        cleanWs()
    }
  }
}
