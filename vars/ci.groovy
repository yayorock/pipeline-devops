import utilities.*

def call(){
    def listStagesOrder = [
        'buildAndTest': 'stageCleanBuildTest',
        'sonar': 'stageSonar',
        'runJar': 'stageRunSpring',
        'rest': 'stageRunTestCurl'
        'nexusCI': 'stageUploadNexus'
    ]

    listStagesOrder.each{
        "${it}"()
    }
    
    def stageCleanBuildTest(){

    }
    def stageSonar(){

    }
    def stageRunSpring(){

    }
    def stageRunTestCurl(){

    }
    def stageUploadNexus(){

    }
}

return this

