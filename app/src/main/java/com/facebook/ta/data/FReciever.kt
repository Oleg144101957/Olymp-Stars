package com.facebook.ta.data

import android.content.Context
import com.facebook.applinks.AppLinkData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FReciever(private val context: Context) {
    suspend fun execute() : String = suspendCoroutine{ c ->
        AppLinkData.fetchDeferredAppLinkData(context){
            c.resume(it?.targetUri.toString())
        }
    }
}