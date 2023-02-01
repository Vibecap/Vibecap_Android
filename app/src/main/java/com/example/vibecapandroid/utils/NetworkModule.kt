package com.example.vibecapandroid.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080" // 서버 주소

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}