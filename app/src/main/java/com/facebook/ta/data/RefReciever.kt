package com.facebook.ta.data

import android.content.Context
import android.os.DeadObjectException
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RefReciever(private val context: Context) {

    suspend fun getRef(): String? = suspendCoroutine{ continiation ->

        val referrerClient = InstallReferrerClient.newBuilder(context).build()

        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(respondCode: Int) {

                if (respondCode == InstallReferrerClient.InstallReferrerResponse.OK){
                    continiation.resume(
                        try {
                            val result = referrerClient?.installReferrer?.installReferrer
                            referrerClient.endConnection()
                            result
                        } catch (e: DeadObjectException){
                            null
                        }
                    )
                } else {
                    continiation.resume(null)
                }

            }

            override fun onInstallReferrerServiceDisconnected() {

            }
        })
    }

}