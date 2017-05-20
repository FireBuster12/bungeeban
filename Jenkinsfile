pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                mvn
            }
        }
        stage('clean') {
            steps {
                mvn clean
            }
        }
    }
}