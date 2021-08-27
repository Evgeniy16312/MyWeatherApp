package com.example.myweatherapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            if (Random.nextBoolean()){
                liveDataToObserve.postValue(AppState.Success("Хорошая погода"))
            }
            else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет интернета")))
            }
        }.start()
    }
}