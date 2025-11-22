pipeline {
    agent any

    tools { maven 'MVN' }

    environment {
        IMAGE_NAME = "iti-service-files"
    }

    stages {
        stage('Build Package') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Build Image') {
            steps {
                script {
                    image = docker.build("uminho/${IMAGE_NAME}:latest", ".")
                }
            }
        }
        stage('Push Image') {
            steps {
                script {
                    docker.withRegistry('http://192.168.56.213:6610', 'OneDev') {
                        image.push()
                    }
                }
            }
        }
    }
}