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
            choice choices: ['Maven', 'Gradle'], description: 'Seleccione herramienta de compilacion', name: 'compileTool'
            text description: 'Enviar los stages separados por ";" ... Vacio si necesita todos los stages', name: 'stages'
        }
        stages {
            stage('Pipeline') {
                steps {
                    script {
                        switch(params.compileTool)
                        {
                            case 'Maven':
                                maven.call(params.stages)
                            break;
                            case 'Gradle':
                                gradle.call(params.stages)
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