pipeline {
    agent any
    tools {
        dockerTool 'docker'
        maven 'maven'
    }
    environment {
        GIT_REPO = "https://gitlab.com/alsvietnam-article-management/alsvietnam-article-management-backend.git"
        REGISTRY = ""
        PROJECT = "alsvietnam-service"
        DOCKER_HUB_LOGIN = credentials('')
    }
    stages {
        stage('Cloning Git') {
            steps {
                git branch: 'production',
                        credentialsId: '',
                        url: GIT_REPO
            }
        }
        stage('Maven build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Building image') {
            steps {
                sh 'docker build -t $PROJECT:latest .'
                sh 'docker tag $PROJECT:latest $REGISTRY/$PROJECT:latest'
            }
        }
        stage('Push Image & Delete Image Origin') {
            steps {
                sh 'docker login --username=$DOCKER_HUB_LOGIN_USR --password=$DOCKER_HUB_LOGIN_PSW'
                sh 'docker push $REGISTRY/$PROJECT:latest'
                sh 'docker rmi $REGISTRY/$PROJECT:latest'
                sh 'docker rmi $PROJECT:latest'
            }
        }
        stage('Deploy Docker') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "4eb2463e-defd-4ac1-a193-c9d4944671f7", passwordVariable: 'sshPwd', usernameVariable: 'sshUser')]) {
                        def remote = [:]
                        remote.name = 'alsvietnam-service-prod'
                        remote.host = ''
                        remote.user = sshUser
                        remote.password = sshPwd
                        remote.allowAnyHosts = true
                        sshCommand remote: remote, command: "docker pull $REGISTRY/$PROJECT:latest"
                        sshCommand remote: remote, command: "cd /home/$PROJECT && docker-compose up -d"
                        sshCommand remote: remote, command: "docker image prune -f"
                    }
                }
            }
        }
    }
}