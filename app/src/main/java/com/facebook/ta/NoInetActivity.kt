package com.facebook.ta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ta.databinding.ActivityNoinetBinding

class NoInetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoinetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoinetBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.refresh.setOnClickListener{
            goToTheSTartActivity()
        }
    }

    private fun goToTheSTartActivity(){
        val intentToTheStartActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToTheStartActivity)
    }

}