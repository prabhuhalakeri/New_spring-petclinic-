def registry = 'https://trialwf69ge.jfrog.io'
pipeline{
    agent any 
    
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-amazon-corretto'
        MAVEN_HOME = '/opt/maven'
    }

    stages {
        stage('checkout') {
            steps {
               git branch: 'main', url: 'https://github.com/prabhuhalakeri/New_spring-petclinic-.git', credentialsId: 'github_credentials'
            }
        }

        stage('build') {
            steps {
                script {
                    sh "${MAVEN_HOME}/bin/mvn clean install"
                }
            
            }
        }
        stage("Jar Publish") {
            steps {
                script {
                        echo '<--------------- Jar Publish Started --------------->'
                        def server = Artifactory.newServer url:registry+"/artifactory" ,  credentialsId:"JFROG_Cred"
                        def properties = "buildid=${env.BUILD_ID},commitid=${GIT_COMMIT}";
                        def uploadSpec = """{
                            "files": [
                                {
                                "pattern": "target/*.jar",
                                "target": "petclinic-libs-release-local/{1}",
                                "flat": "false",
                                "props" : "${properties}",
                                "exclusions": [ "*.sha1", "*.md5"]
                                }
                            ]
                        }"""
                        def buildInfo = server.upload(uploadSpec)
                        buildInfo.env.collect()
                        server.publishBuildInfo(buildInfo)
                        echo '<--------------- Jar Publish Ended --------------->'  
            
            }
        }   
    }           
    }
}
