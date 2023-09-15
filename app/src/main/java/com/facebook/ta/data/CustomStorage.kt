package com.facebook.ta.data

import com.facebook.ta.Constants
import io.paperdb.Paper

class CustomStorage(){
    fun readData(key: String): String{
        return Paper.book().read<String>(key) ?: Constants.EMPTY
    }

    fun saveData(key: String, data: String){
        Paper.book().write(key, data)
    }

    fun readTimes(): Int{
        return Paper.book().read<Int>(Constants.KEY_TIMES) ?: 0
    }

    fun increaseTimes(){
        val currentTimes = Paper.book().read<Int>(Constants.KEY_TIMES) ?: 0
        Paper.book().write(Constants.KEY_TIMES, currentTimes+1)
    }
}