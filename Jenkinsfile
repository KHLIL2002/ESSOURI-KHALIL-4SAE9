pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        IMAGE_NAME = 'khalilessouri/student-management'
        SONARQUBE = 'SonarQube'
        K8S_NAMESPACE = 'devops'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=student -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN'
                }
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

       stage('Deploy to Kubernetes') {
            steps {
                script {

                   sh 'kubectl apply -f k8s/mysql-deployment.yaml -n ${K8S_NAMESPACE}'
                   sh 'kubectl apply -f k8s/spring-deployment.yaml -n ${K8S_NAMESPACE}'

                   sh 'kubectl rollout restart deployment/spring-app -n ${K8S_NAMESPACE}'
               }
           }
       }
    }

    post {
        success {
            echo 'Pipeline SUCCESS ✔ - Application Deployed to Kubernetes'
        }
        failure {
            echo 'Pipeline FAILED ❌'
        }
    }
}