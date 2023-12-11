pipeline {
    agent any
    tools {
        jdk '19'
        gradle 'Gradle 8.4'
    }
    stages {
        stage('Build'){
            steps {
                echo 'Building User Management Project'
            }
        }
        stage('Test') {
            steps {
                echo 'Running User Management Project Tests'
            }
            post {
                always {
                    junit "build/test-results/test/**/*.xml"
                }
            }
        }
    }
}
