package com.example.weatherkotlin.data.repository

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        println("Sending request: ${request.url}")
        Log.d("Sending request:", request.url.toString())

        // Tiếp tục với yêu cầu
        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        println("Received response for: ${response.request.url} in ${(t2 - t1) / 1e6} ms")
        Log.d("Received response for: ",response.request.url.toString())
        return response
    }
}
