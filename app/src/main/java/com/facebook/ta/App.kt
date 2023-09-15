package com.facebook.ta

import android.app.Application
import android.util.Log
import com.facebook.ta.data.CustomStorage
import com.onesignal.OneSignal
import io.paperdb.Paper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initStorage()
        initSignal()
        times()
    }

    private fun times() {
        val customStorage = CustomStorage()
        customStorage.increaseTimes()
    }

    private fun initSignal() {
        val constants = Constants(this)
        val devKey = constants.getOneSignal(0,0)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(devKey)
    }

    private fun initStorage() {
        Paper.init(this)
    }

    companion object{
        val customDDbTransmitter = MutableSharedFlow<String>()
        val customLinkFlow = MutableSharedFlow<String>()
    }
}