package com.facebook.ta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ta.data.SuperChecker
import com.facebook.ta.databinding.ActivityMain2Binding
import kotlin.system.exitProcess

class Main2Activity :  AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListenners()
    }

    private fun setClickListenners() {
        binding.btn1.setOnClickListener {
            //Play
            val intentToPlayground = Intent(this, Main3Activity::class.java)
            startActivity(intentToPlayground)
        }

        binding.btn2.setOnClickListener {
            //Exit
            exit()
        }
    }

    private fun exit(){
        val superChecker = SuperChecker(this)
        val res = superChecker.execute("qqq", 12)
        if (res){
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            exitProcess(0)
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            finish()
            exitProcess(0)
        }
    }

    override fun onBackPressed() {
        //off
    }
}