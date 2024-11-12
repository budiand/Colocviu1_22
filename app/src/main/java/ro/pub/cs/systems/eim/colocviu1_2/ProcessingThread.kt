package ro.pub.cs.systems.eim.colocviu1_2


import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*
import kotlin.math.sqrt

class ProcessingThread(
    private val context: Context,
    private val sum: Int
) : Thread() {

    private var isRunning = true
    private val summ = sum

    override fun run() {
        Log.d(Constants.BROADCAST_RECEIVER_TAG, "Thread started! PID: ${android.os.Process.myPid()}, TID: ${android.os.Process.myTid()}")
        while (isRunning) {
            sendMessage()
            sleepThread()
        }
        Log.d(Constants.BROADCAST_RECEIVER_TAG, "Thread stopped!")
    }

    private fun sendMessage() {
        val intent = Intent().apply {
            action = Constants.actionTypes.random()  // Acțiunea trebuie să fie una validă
            putExtra(Constants.BROADCAST_RECEIVER_EXTRA, "Time: ${Date()}, Sum: $summ")
        }
        context.sendBroadcast(intent)
    }


    private fun sleepThread() {
        try {
            sleep(2000)  // 2 seconds
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun stopThread() {
        isRunning = false
    }
}
