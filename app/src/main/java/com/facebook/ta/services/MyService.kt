package com.facebook.ta.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.facebook.ta.Constants

class MyService : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //get adb and send to App Flow

        val constants = Constants(this)
        val ddb = constants.getOneSignal(2, 3)


        //send adb by broadcast
        val intent1 = Intent(constants.getOneSignal(12, 12))
        intent1.putExtra(constants.getOneSignal(21,22), ddb)
        sendBroadcast(intent1)

        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}