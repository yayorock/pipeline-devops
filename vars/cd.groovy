import utilities.*

def call(){
    def listStagesOrder = [
        'downloadNexus': 'stageDownloadNexus',
        'runDownloadedJar': 'stageRunJar',
        'rest': 'stageRunTestCurl'
        'nexusCD': 'stageUploadNexus'
    ]

    listStagesOrder.each{
        "${it}"()
    }
}

def stageDownloadNexus(){

}
def stageRunJar(){

}
def stageRunTestCurl(){

}
def stageUploadNexus(){

}

return this