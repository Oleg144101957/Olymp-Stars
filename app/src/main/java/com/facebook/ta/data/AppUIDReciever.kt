package com.facebook.ta.data

import android.content.Context
import com.appsflyer.AppsFlyerLib
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppUIDReciever(private val context: Context) {
    suspend fun provideKanielUID(): String = suspendCoroutine { continuation ->
        val result = AppsFlyerLib.getInstance().getAppsFlyerUID(context).toString()
        continuation.resume(result)
    }

}