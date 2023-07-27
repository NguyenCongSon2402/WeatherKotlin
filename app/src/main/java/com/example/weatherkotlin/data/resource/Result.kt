package com.example.weatherkotlin.data.resource

import java.lang.Exception

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Exception) : Result<T>()
}