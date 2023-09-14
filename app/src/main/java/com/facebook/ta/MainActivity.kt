package com.facebook.ta

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.facebook.ta.data.SuperChecker
import com.facebook.ta.databinding.ActivityMainBinding
import com.facebook.ta.services.MyService
import io.paperdb.Paper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //loading
    private lateinit var binding: ActivityMainBinding
    private lateinit var customBroadcast: BroadcastReceiver




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkData()

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
        //TODO add listenners only first time
        addListenners()
    }

    private fun startDevServiceAndRegisterReciever(a: Int, b:Int, constants: Constants){
        val intentForService = Intent(this, MyService::class.java)
        startService(intentForService)

        val intentDevBroadcast = IntentFilter(constants.getOneSignal(23, 23))
        registerReceiver(customBroadcast, intentDevBroadcast)

    }


    private fun checkData(){
        val superChecker = SuperChecker(this)
        val checkRes = superChecker.execute("led", 2)
        if (checkRes){
            goToTheSecretActivity()
        }
    }

    private fun goToTheSecretActivity() {
        val intent = Intent(this, Main2Activity::class.java)
        val scope = MainScope()

        scope.launch {
            delay(1800)
            startActivity(intent)
        }
    }

    private fun addListenners(){

        lifecycleScope.launch {
            App.customDDbTransmitter.collect{
                Log.d("123123", "Custom trasmitter $it")


                //check adb
                if (it == "1"){
                    //get data from track

                    val getData = Paper.book().read<String>("112")
                    Log.d("123123", "Data from Paper $getData")


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

}