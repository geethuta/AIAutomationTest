pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        bat 'mvn test'
                    } catch (Exception e) {
                        currentBuild.result = 'UNSTABLE'
                        echo "Tests failed but marking as unstable to continue pipeline"
                    }
                }
            }
        }

        stage('Generate Reports') {
            steps {
                junit '**/target/surefire-reports/*.xml'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/surefire-reports',
                    reportFiles: 'emailable-report.html',
                    reportName: 'TestNG Report',
                    reportTitles: 'TestNG Report'
                ])
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        unstable {
            echo 'Pipeline completed with test failures!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
} 
