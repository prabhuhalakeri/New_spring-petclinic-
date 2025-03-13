pipeline{
    agent any 
    
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-amazon-corretto'
        MAVEN_HOME = '/opt/maven'
    }

    stages {
        stage('checkout') {
            steps {
               git branch: 'Develop', url: 'https://github.com/prabhuhalakeri/New_spring-petclinic-.git', credentialsId: 'github_cred_01'
            }
        }

        stage('build') {
            steps {
                script {
                    sh "${MAVEN_HOME}/bin/mvn clean install"
                }
            
            }
        }     
    }
}
