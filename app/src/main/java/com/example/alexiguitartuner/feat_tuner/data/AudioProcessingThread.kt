package com.example.alexiguitartuner.feat_tuner.data

class AudioProcessingThread(target: Runnable?) : Thread(target) {

    @Volatile
    var isRunning = false

    override fun start() {
        if (!isRunning) {
            isRunning = true
            super.start()
        }
    }

    override fun run() {
        super.run()
        if(!isRunning) return
    }

    fun exit(){
        if (isRunning) isRunning = false
    }
}