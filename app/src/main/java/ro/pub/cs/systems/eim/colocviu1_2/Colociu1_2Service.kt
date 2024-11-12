package ro.pub.cs.systems.eim.colocviu1_2

import android.app.Service
import android.app.Service.START_REDELIVER_INTENT
import android.content.Intent
import android.os.IBinder

class Colociu1_2Service : Service() {
    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Retrieve the numbers from the intent extras
        val sum = intent.getIntExtra(Constants.KEY_SUM, 0)

        // Start the processing thread to handle calculations and broadcasts
        processingThread = ProcessingThread(this, sum)
        processingThread?.start()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        // Stop the thread when the service is destroyed
        processingThread?.stopThread()
        super.onDestroy()
    }
}
