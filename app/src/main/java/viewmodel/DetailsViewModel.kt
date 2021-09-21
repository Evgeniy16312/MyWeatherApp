package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import model.WeatherDTO
import model.repository.DetailsRepository
import model.repository.DetailsRepositoryImpl
import model.repository.RemoteDataSource
import model.utils.convertDtoToModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callBack)
    }

    private val callBack = object : Callback {

        @Throws(IOException::class)
        override fun onResponse(call: Call?, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call?, e: IOException?) {
            detailsLiveData.postValue(AppState.Error(Throwable(e?.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: String): AppState {
            val weatherDTO: WeatherDTO =
                Gson().fromJson(serverResponse, WeatherDTO::class.java)
            val fact = weatherDTO.fact
            return if (fact?.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel(weatherDTO))
            }
        }
    }
}
