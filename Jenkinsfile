pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'     // Vérifie que c'est bien le nom dans Jenkins -> Tools
        maven 'M2_HOME'      // Vérifie que c'est bien le nom dans Jenkins -> Tools
    }

    environment {
        IMAGE_NAME = 'khalilessouri/student-management'
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

        // --- CORRECTION 1 : On ajoute l'étape de Test ici pour le Coverage ---
        stage('Run Tests') {
            steps {
                // On fait le 'clean' ici
                sh 'mvn clean test'
            }
        }

        // --- CORRECTION 2 : On package sans supprimer le rapport de test ---
        stage('Build') {
            steps {
                // On enlève 'clean' et on skip les tests pour aller vite (déjà faits au dessus)
                sh 'mvn package -DskipTests'
            }
        }

        stage('Test, Analyze & Build') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    // 'verify' lance les tests et crée le rapport JaCoCo
                    // 'sonar:sonar' lit le rapport immédiatement
                    // '-DskipTests' n'est PAS là, car on veut que les tests tournent !
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=student'
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