package com.example.myweatherapp.ui.main

sealed class AppState {
    data class Success(val weatherData: String) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}