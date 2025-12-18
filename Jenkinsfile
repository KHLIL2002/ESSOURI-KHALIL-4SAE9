pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        IMAGE_NAME = 'khalilessouri/student-management'
        // Just use the name defined in Jenkins Configuration, no need for variables here usually
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

        // --- IMPROVED UNIT TEST STAGE ---
        stage('Unit Tests & Coverage') {
            steps {
                // We do 'clean' HERE to start fresh.
                // We run 'test' to execute Junit tests.
                // If JaCoCo is in pom.xml, this generates the report.
                sh 'mvn clean test'
            }
        }

        // --- IMPROVED BUILD STAGE ---
        stage('Build JAR') {
            steps {
                // DO NOT use 'clean' here, or you lose the test reports!
                // We use '-DskipTests' to avoid running tests a second time (saves time).
                sh 'mvn package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Ensure the name here matches exactly what you set in Jenkins > Manage > System
                withSonarQubeEnv('SonarQube-Server') {
                    // No need to pass login/password manually if 'withSonarQubeEnv' is configured correctly
                    sh 'mvn sonar:sonar'
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
                sh "docker build -t ${IMAGE_NAME}:latest ."
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
                // Assuming you added the K8S credential file as 'k8s-config'
                withCredentials([file(credentialsId: 'k8s-config', variable: 'KUBECONFIG')]) {
                    script {
                        sh "kubectl apply -f k8s/mysql-deployment.yaml -n ${K8S_NAMESPACE}"
                       sh "kubectl apply -f k8s/spring-deployment.yaml -n ${K8S_NAMESPACE}"

                       // Force restart to pull the new image
                       sh "kubectl rollout restart deployment/spring-app -n ${K8S_NAMESPACE}"
                   }
                }
           }
       }
    }

    post {
        success {
            echo 'Pipeline SUCCESS ✔ - Application Deployed'
        }
        failure {
            echo 'Pipeline FAILED ❌'
        }
    }
}