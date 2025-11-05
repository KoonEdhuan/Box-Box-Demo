package com.example.boxboxdemo.data.service

import com.example.boxboxdemo.data.model.DriversList
import com.example.boxboxdemo.data.model.RaceInfo
import retrofit2.http.GET

interface MyApiService {

    @GET("e8616da8-220c-4aab-a670-ab2d43224ecb")
    suspend fun getDriversRanking(): DriversList

    @GET("9086a3f1-f02b-4d24-8dd3-b63582f45e67")
    suspend fun getRacesInfo(): RaceInfo
}