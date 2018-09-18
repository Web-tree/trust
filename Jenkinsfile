pipeline {
    agent {
        docker {
            image 'maven:3'
            args '-v jenkins_m2:/root/.m2'
        }
    }
    stages {
        stage('build') {
            steps {
                parallel(
                        back: {
                            dir('back') {
                                sh 'mvn -B clean verify'
                            }
                        },
                        web: {
                            dir('front') {
                                sh 'mvn -B verify'
                            }
                        }
                )
            }
        }
    }
}