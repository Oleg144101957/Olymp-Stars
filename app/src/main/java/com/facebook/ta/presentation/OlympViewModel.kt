package com.facebook.ta.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.ta.R
import com.facebook.ta.domain.Olympic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class OlympViewModel : ViewModel() {

    val liveElementsList: MutableLiveData<List<Olympic>> = MutableLiveData()
    val liveIsListAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val liveScores: MutableStateFlow<Int> = MutableStateFlow(0)



    private val _timeFlow = MutableStateFlow(0)
    val timeFlow: StateFlow<Int> = _timeFlow.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {  // Adjust this condition based on your need
                delay(1000L) // Delay for 1 second
                val current = _timeFlow.value
                _timeFlow.emit(current + 1) // Emit next value
            }
        }
    }



    fun initListInViewModel(){
        val newList = images.shuffled()
        val newOlympicList: MutableList<Olympic> = mutableListOf()

        for (i in 0..19){
            newOlympicList.add(i, Olympic(id = i, draw = newList[i], isExist = true, name = "i$i"))
        }

        liveElementsList.value = newOlympicList
    }

    private fun exchange(position: Int, positionToExchange: Int){


        if (liveIsListAvailable.value == true){
            liveIsListAvailable.value = false
            viewModelScope.launch {
                val one = liveElementsList.value?.get(position)?.draw
                val two = liveElementsList.value?.get(positionToExchange)?.draw

                val newMutableList = liveElementsList.value?.map {
                    if(it.id == position){
                        it.copy(draw = two!!)
                    } else if (it.id == positionToExchange){
                        it.copy(draw = one!!)
                    } else {
                        it
                    }
                }
                liveElementsList.value = newMutableList
                checkTheSameOlympics()
            }
        }
    }


    fun getRandomElementFromList(images: List<Int>): Int {
        val randomIndex = Random.nextInt(images.size)
        return images[randomIndex]
    }

    private suspend fun setFalseToTheSameElements(mutableListToSetFalse: MutableList<Int>){
        val newList = liveElementsList.value?.map {
            if (mutableListToSetFalse.contains(it.id)){
                it.copy(isExist = false)
            } else {
                it
            }
        }

        liveElementsList.value = newList


        delay(900)
        val listWithNewImagesAndTrueArguments = liveElementsList.value?.map {
            if (mutableListToSetFalse.contains(it.id)){
                it.copy(isExist = true, draw = getRandomElementFromList(images))
            } else {
                it
            }
        }

        liveElementsList.value = listWithNewImagesAndTrueArguments
    }


    private fun checkTheSameOlympics(){

        val mutSetRow1 = mutableSetOf<Int>()
        val mutSetRow2 = mutableSetOf<Int>()
        val mutSetRow3 = mutableSetOf<Int>()
        val mutSetRow4 = mutableSetOf<Int>()
        val mutSetRow5 = mutableSetOf<Int>()

        val mutableListToSetFalse = mutableListOf<Int>()

        for (i in 0..3){
            liveElementsList.value?.get(i)?.draw?.let { mutSetRow1.add(it) }
        }

        for (i in 4..7){
            liveElementsList.value?.get(i)?.draw?.let { mutSetRow2.add(it) }
        }

        for (i in 8..11){
            liveElementsList.value?.get(i)?.draw?.let { mutSetRow3.add(it) }
        }

        for (i in 12..15){
            liveElementsList.value?.get(i)?.draw?.let { mutSetRow4.add(it) }
        }

        for (i in 16..19){
            liveElementsList.value?.get(i)?.draw?.let { mutSetRow5.add(it) }
        }

        if (mutSetRow1.size == 1){
            increaseScores()

            mutableListToSetFalse.add(0)
            mutableListToSetFalse.add(1)
            mutableListToSetFalse.add(2)
            mutableListToSetFalse.add(3)

        }

        if (mutSetRow2.size == 1){
            increaseScores()

            mutableListToSetFalse.add(4)
            mutableListToSetFalse.add(5)
            mutableListToSetFalse.add(6)
            mutableListToSetFalse.add(7)

        }

        if (mutSetRow3.size == 1){
            increaseScores()

            mutableListToSetFalse.add(8)
            mutableListToSetFalse.add(9)
            mutableListToSetFalse.add(10)
            mutableListToSetFalse.add(11)

        }

        if (mutSetRow4.size == 1){
            increaseScores()

            mutableListToSetFalse.add(12)
            mutableListToSetFalse.add(13)
            mutableListToSetFalse.add(14)
            mutableListToSetFalse.add(15)

        }

        if (mutSetRow5.size == 1){
            increaseScores()

            mutableListToSetFalse.add(16)
            mutableListToSetFalse.add(17)
            mutableListToSetFalse.add(18)
            mutableListToSetFalse.add(19)
        }

        viewModelScope.launch {
            setFalseToTheSameElements(mutableListToSetFalse)
            liveIsListAvailable.value = true
        }
    }



    private fun increaseScores(){
        liveScores.value = liveScores.value + 1
    }

    fun upTransfer(position: Int){
        if (position in 0..3){
            return
        } else {
            val positionToExchange = position - 4
            exchange(position, positionToExchange)
        }
    }

    fun downTransfer(position: Int){
        if (position in 16..19){
            return
        } else {
            val positionToExchange = position + 4
            exchange(position, positionToExchange)
        }

    }

    fun rightTransfer(position: Int){
        if (position == 3 || position == 7 || position == 11 || position == 15 || position == 19){
            return
        } else {
            val positionToExchange = position + 1
            exchange(position, positionToExchange)
        }
    }

    fun leftTransfer(position: Int){
        if (position == 0 || position == 4 || position == 8 || position == 12 || position == 16){
            return
        } else {
            val positionToExchange = position - 1
            exchange(position, positionToExchange)
        }
    }

    companion object{
        const val ONE_SIG = "fwefdsdffsdf"
        const val APPS = "fawaefweaf"

        val images = listOf(
            R.drawable.element_gold_2,
            R.drawable.element_korona_5,
            R.drawable.element_kubok_4,
            R.drawable.element_rubin_3,
            R.drawable.element_time_6,
            R.drawable.element_gold_2,
            R.drawable.element_korona_5,
            R.drawable.element_kubok_4,
            R.drawable.element_rubin_3,
            R.drawable.element_time_6,
            R.drawable.element_gold_2,
            R.drawable.element_korona_5,
            R.drawable.element_kubok_4,
            R.drawable.element_rubin_3,
            R.drawable.element_time_6,
            R.drawable.element_gold_2,
            R.drawable.element_gold_2,
            R.drawable.element_korona_5,
            R.drawable.element_kubok_4,
            R.drawable.element_rubin_3)
    }
}