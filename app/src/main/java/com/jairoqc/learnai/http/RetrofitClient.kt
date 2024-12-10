package com.jairoqc.learnai.http

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://159.203.186.177:8080/"

    val instance: ApiService by lazy {
        val gson = GsonBuilder()
            .setLenient()  // Para manejar respuestas JSON mal formateadas
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)  // Crear el servicio aquí
    }
}
