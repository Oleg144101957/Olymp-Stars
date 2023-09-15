package com.facebook.ta.data

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.facebook.ta.Constants
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AandGReciever(private val context: Context) {


    private val constants = Constants(context)

    suspend fun execute(small: Long, big: Long) : Any {
        val apps = getApp()
        val google = getG()
        val googleAndAppsMap: MutableMap<String, Any> = mutableMapOf()
        googleAndAppsMap[Constants.G] = google
        if (apps != null) {
            googleAndAppsMap[Constants.A] = apps
        }

        return if (small<big) googleAndAppsMap else Any()
    }


    private suspend fun getApp() : MutableMap<String, Any>? = suspendCoroutine { c ->
        val devKey = constants.getAppsDevKey(1,1)

        AppsFlyerLib.getInstance().init(devKey, AListenner{
              c.resume(it)
        }, context).start(context)
    }

    private suspend fun getG() : String = withContext(Dispatchers.IO){
        AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
    }

}