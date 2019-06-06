pipeline {
    agent any
    stages {
        stage('PR verify') {
            parallel {
                stage('Verify back') {
                    environment {
                        MAVEN_HOME = '/usr/share/maven'
                    }
                    agent {
                        kubernetes {
                            label 'mystuff-validate-maven'
                            containerTemplate {
                                name 'maven'
                                image 'maven-jdk-8'
                                ttyEnabled true
                                command 'cat'
                            }
                        }
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
                        kubernetes {
                            label 'mystuff-validate-node'
                            containerTemplate {
                                name 'node'
                                image 'webtree/node-with-chrome'
                                ttyEnabled true
                                command 'cat'
                            }
                        }
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
                        stage('Validate') {
                            steps {
                                dir('front/') {
                                    sh 'npm ci'
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