package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Repository
import model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()

) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    fun getWeatherFromRemoteSource() = getDataFromLocalSource()


    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет интернета")))
            }
        }.start()
    }
}