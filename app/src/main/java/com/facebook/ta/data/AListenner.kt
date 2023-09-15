package com.facebook.ta.data

import com.appsflyer.AppsFlyerConversionListener

class AListenner(private val dataReciever: (MutableMap<String, Any>?) -> Unit) : AppsFlyerConversionListener{


    override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
        dataReciever(p0)
    }

    override fun onConversionDataFail(p0: String?) {
        dataReciever(null)
    }

    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
        dataReciever(null)
    }

    override fun onAttributionFailure(p0: String?) {
        dataReciever(null)
    }
}