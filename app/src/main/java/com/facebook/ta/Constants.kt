package com.facebook.ta

import android.content.Context
import android.provider.Settings

class Constants(private val context: Context) {

    private val oneSignalAppID = listOf("96772a63-32fc-46e1-", "91ee-bda392552763")

    fun getOneSignal(number1: Int, number2: Int) : String {

        val sum = number1+number2
        val ddb = Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED)

        return when(sum){
            0 -> return oneSignalAppID[0]+oneSignalAppID[1]
            5 -> return ddb
            else -> return oneSignalAppID[1]
        }
    }
}