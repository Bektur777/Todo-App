pipeline {

    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {

        stage('build') {

            steps {
                script {
                    def modules = ['task-service', 'admin-service']
                    for (module in modules) {
                        dir(module) {
                            echo "Building ${module}"
                            sh 'mvn clean install'
                        }
                    }
                }
            }
        }

        stage('test') {

            steps {
                script {
                    def modules = ['task-service', 'admin-service']
                    for (module in modules) {
                        dir(module) {
                            echo "Testing ${module}"
                            sh 'mvn test'
                        }
                    }
                }
            }
        }

        stage('deploy') {

            steps {
                echo 'deploy...'
            }
        }
    }
}