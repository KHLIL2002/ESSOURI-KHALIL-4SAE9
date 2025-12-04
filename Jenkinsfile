pipeline {
    agent any

    tools {
        jdk 'Java17'        // Le nom configuré dans Jenkins (à adapter)
        maven 'Maven3'     // Nom Maven dans Jenkins (à adapter)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build SUCCESS ✔'
        }
        failure {
            echo 'Build FAILED ❌'
        }
    }
}
