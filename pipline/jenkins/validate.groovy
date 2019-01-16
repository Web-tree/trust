pipeline {
    agent {
        label 'slave'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    def branch = env.CHANGE_BRANCH ? env.CHANGE_BRANCH : env.GIT_BRANCH
                    git branch: "${branch}", credentialsId: 'github-app', url: 'https://github.com/Web-tree/trust.git'
                }
            }
        }
        stage('PR verify') {
            parallel {
                stage('Verify back') {
                    agent {
                        docker {
                            image 'maven:3.6.0-jdk-8-alpine'
                            args '-v jenkins_m2:/root/.m2'
                            reuseNode true
                        }
                    }
                    stages {
                        stage('Validate') {
                            steps {
                                dir('back') {
                                    sh 'mvn -B clean verify -Dmaven.test.failure.ignore=true'
                                    junit '**/target/surefire-reports/**/*.xml'
                                }
                            }
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
                    stages {
                        stage('Validate') {
                            steps {
                                dir ('front/') {
                                    sh 'npm i'
                                    sh 'npm run test-headless'
                                    junit 'testResult/*.xml'
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}