pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = 'khalilessouri/student-management'
        SONARQUBE = 'SonarQube'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                       echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                       docker push ${IMAGE_NAME}:latest
                       docker logout
                   '''
               }
           }
       }

       stage('Deploy with Docker Compose') {
            steps {
                sh '''

                  # Build and start the containers
                  docker-compose up -d --build

                  # Show running containers
                  docker ps
               '''
           }
       }
    }

     stage('SonarQube Analysis') {
        steps {
            withSonarQubeEnv('SonarQube') {  // matches SONARQUBE name
                    sh 'mvn sonar:sonar -Dsonar.projectKey=student-management -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN'
                }
            }
        }

    post {
        success {
            echo 'Pipeline SUCCESS ✔'
        }
        failure {
            echo 'Pipeline FAILED ❌'
        }
    }
}
