def call(branch){
  
    pipeline {
        agent any
        environment {
            NEXUS_USER         = credentials('nexus_user')
            NEXUS_PASSWORD     = credentials('nexus_pass')
        }
        stages {
            stage('Pipeline') {
                figlet " Gradle "
                steps {
                    script {
                        switch(branch)
                        {
                            case 'develop':
                                ci.call()
                            break;
                            case 'release':
                                cd.call()
                            break;
                        }
                    }
                }
                post {
                    success{
                        slackSend color: 'good', message: "Gonzalo Muñoz: [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack', channel: 'U02MGMLHSPQ'
                    }
                    failure{
                        slackSend color: 'danger', message: "Gonzalo Muñoz: [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack', channel: 'U02MGMLHSPQ'
                    }
                }
            }
        }
    }
}

return this;