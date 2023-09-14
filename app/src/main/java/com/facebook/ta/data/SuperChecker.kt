package com.facebook.ta.data

import android.content.Context
import android.provider.Settings
import android.util.Log

class SuperChecker(private val context: Context){
    fun execute(data: String, subData: Int): Boolean{
        val result = subData * 33
        logResult(result)

        return true
    }

    private fun logResult(result: Int){
        Log.d("info", "Result $result")
    }
}