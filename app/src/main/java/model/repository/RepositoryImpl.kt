package model.repository

import model.Weather
import model.getRussianCities
import model.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}