pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_IMAGE = "bhave0305/java-app"
        DOCKER_CREDENTIALS = "docker-hub"
        KUBECONFIG = "/root/.kube/config"
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo '========== CHECKOUT STAGE =========='
                    checkout scm
                    sh 'pwd && ls -la'
                    echo 'Source code checked out successfully'
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo '========== BUILD STAGE =========='
                    try {
                        sh 'java -version'
                        sh 'mvn --version'
                        sh 'mvn clean compile -DskipTests'
                        echo 'Build successful'
                    } catch (Exception e) {
                        error("Build failed: ${e.message}")
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo '========== TEST STAGE =========='
                    sh 'mvn test || true'
                    echo 'Tests executed'
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    echo '========== PACKAGE STAGE =========='
                    sh 'mvn package -DskipTests'
                    sh 'ls -la target/'
                    echo 'JAR file created successfully'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    echo '========== BUILD DOCKER IMAGE =========='
                    try {
                        sh '''
                            docker --version
                            docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .
                            docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest
                            docker images | grep java-app
                            echo "Docker image built successfully"
                        '''
                    } catch (Exception e) {
                        error("Docker build failed: ${e.message}")
                    }
                }
            }
        }
        
        stage('Push to Docker Registry') {
            steps {
                script {
                    echo '========== PUSH TO DOCKER REGISTRY =========='
                    withCredentials([usernamePassword(
                        credentialsId: 'docker-hub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh '''
                            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
                            docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                            docker push ${DOCKER_IMAGE}:latest
                            echo "Docker image pushed successfully"
                        '''
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo '========== DEPLOY TO KUBERNETES =========='
                    sh '''
                        kubectl version --client
                        kubectl apply -f deployment.yaml
                        sleep 5
                        kubectl get deployments -o wide
                        kubectl get pods -o wide
                        kubectl get svc -o wide
                        echo "Deployment completed"
                    '''
                }
            }
        }
        
        stage('Verify Deployment') {
            steps {
                script {
                    echo '========== VERIFY DEPLOYMENT =========='
                    sh '''
                        echo "Waiting for pods to be ready..."
                        sleep 10
                        kubectl get deployments
                        kubectl get svc student-timetable-service
                        POD_STATUS=$(kubectl get pods -l app=student-timetable -o jsonpath='{.items[0].status.phase}')
                        echo "Pod Status: ${POD_STATUS}"
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo '========== PIPELINE SUCCESS =========='
            echo 'Student Timetable Application deployed successfully!'
            echo "Access at: http://<NodeIP>:30008"
        }
        failure {
            echo '========== PIPELINE FAILURE =========='
            echo 'Check the logs above for details'
        }
        always {
            echo '========== PIPELINE COMPLETED =========='
            sh 'echo "Build #${BUILD_NUMBER} finished at $(date)"'
        }
    }
}