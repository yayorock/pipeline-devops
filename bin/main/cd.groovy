import utilities.*

def call(){

    figlet " Despliegue Continuo "

    def listStagesOrder = [
        'downloadNexus': 'stageDownloadNexus',
        'runDownloadedJar': 'stageRunJar',
        'rest': 'stageRunTestCurl',
        'nexusCD': 'stageUploadNexus'
    ]

    listStagesOrder.each{
        "${it.value}"()
    }
}

def stageDownloadNexus(){
    env.DESCRTIPTION_STAGE = 'Descargar Nexus'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "download_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
}
def stageRunJar(){
    env.DESCRTIPTION_STAGE = 'Levantar Artefacto Jar'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "run_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'nohup java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
}
def stageRunTestCurl(){
    env.DESCRTIPTION_STAGE = 'Testear Artefacto - Dormir Esperar 20sg '
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "curl_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}
def stageUploadNexus(){
    env.DESCRTIPTION_STAGE = 'Subir a nexus '
    stage("${env.DESCRTIPTION_STAGE}") {
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: 'jar',
                    filePath: 'build/DevOpsUsach2020-0.0.1.jar'
                ]
            ],
                mavenCoordinate: [
                    artifactId: 'DevOpsUsach2020',
                    groupId: 'com.devopsusach2020',
                    packaging: 'jar',
                    version: '0.0.1'
                ]
            ]
        ]
    }
}

return this;