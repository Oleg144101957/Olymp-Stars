package com.facebook.ta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ta.databinding.ActivityMain4Binding
import com.facebook.ta.presentation.OlympViewModel
import com.facebook.ta.presentation.OlympViewModel.Companion.ONE_SIG

class Main4Activity : AppCompatActivity() {


    private lateinit var binding: ActivityMain4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val scores = intent.getIntExtra(ONE_SIG, 0)
        binding.tvScores.text = "$scores"


        binding.closeBtn.setOnClickListener {
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }

    }


}