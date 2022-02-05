import utilities.*

def call(){

    figlet " Integracion Continua "

    def listStagesOrder = [
        'buildAndTest': 'stageCleanBuildTest',
        'sonar': 'stageSonar',
        'runJar': 'stageRunSpring',
        'rest': 'stageRunTestCurl',
        'nexusCI': 'stageUploadNexus'
    ]

    listStagesOrder.each{
        "${it}"()
    }
}   
def stageCleanBuildTest(){
    env.DESCRTIPTION_STAGE = 'Build - Test'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "build - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'gradle clean build'
    }
}
def stageSonar(){
    env.DESCRTIPTION_STAGE = 'Paso 2: Sonar - Análisis Estático'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "sonar - ${DESCRTIPTION_STAGE}"
        withSonarQubeEnv('sonarqube') {
            sh './gradlew sonarqube -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
        }
    }
}
def stageRunSpring(){
    env.DESCRTIPTION_STAGE = 'Paso 3: Init Springboot Gradle'
    stage("${env.DESCRTIPTION_STAGE}") {
        env.STAGE = "run_spring_curl - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'gradle bootRun&'
    }
}
def stageRunTestCurl(){
    env.DESCRTIPTION_STAGE = 'Paso 4: Curl Springboot Gralde sleep 20'
    stage("${env.DESCRTIPTION_STAGE}") {
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}
def stageUploadNexus(){
    nexusPublisher nexusInstanceId: 'nexus',
    nexusRepositoryId: 'devops-usach-nexus',
    packages: [
        [
            $class: 'MavenPackage',
            mavenAssetList: [
                [classifier: '',
                extension: 'jar',
                filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']
            ],
            mavenCoordinate: [
                artifactId: 'DevOpsUsach2020',
                groupId: 'com.devopsusach2020',
                packaging: 'jar',
                version: '0.0.1']
        ]
    ]
}   


return this;

