pipeline {
    agent any
    stages {
        stage('PR verify') {
            parallel {
                stage('Verify back') {
                    agent {
                        docker {
                            image 'maven:3'
                            args '-v jenkins_m2:/root/.m2'
                            reuseNode true
                        }
                    }
                    steps {
                        dir('back') {
                            sh 'mvn -B clean verify'
                        }
                    }
                }
                stage('Verify front') {
                    agent {
                        docker {
                            image 'webtree/node-with-chrome'
                            reuseNode true
                        }
                    }
                    steps {
                        dir('front') {
                            sh 'npm i'
                            sh 'npm run test-headless'
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            junit 'front/testResult/**/*.xml'
            junit 'back/**/target/surefire-reports/**/*.xml'
        }
    }
}