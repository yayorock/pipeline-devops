/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
    pipeline {
        agent any
        environment {
            NEXUS_USER         = credentials('nexus_user')
            NEXUS_PASSWORD     = credentials('nexus_pass')
        }
        parameters {
            choice(
                name: 'compileTool',
                choices: ['Maven', 'Gradle'],
                description : 'Seleccione herramienta de compilacion'
            )
        }
        stages {
            stage('Pipeline') {
                steps {
                    script {
                        switch(params.compileTool)
                        {
                            case 'Maven':
                                //def ejecucion = load 'maven.groovy'
                                //ejecucion.call()
                                maven.call()
                            break;
                            case 'Gradle':
                                //def ejecucion = load 'gradle.groovy'
                                //ejecucion.call()
                                gradle.call()
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