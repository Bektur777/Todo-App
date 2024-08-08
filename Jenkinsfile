pipeline {

    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {

        stage('build') {

            steps {
                def modules = ['task-service', 'admin-service']
                for (module in modules) {
                    sh "mvn -pl ${module} clean install"
                }
            }
        }

        stage('test') {

            steps {
                def modules = ['task-service', 'admin-service']
                for (module in modules) {
                    sh "mvn -pl ${module} test"
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