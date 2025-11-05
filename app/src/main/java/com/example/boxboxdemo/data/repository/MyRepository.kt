package com.example.boxboxdemo.data.repository

import com.example.boxboxdemo.data.model.DriversList
import com.example.boxboxdemo.data.model.RaceInfo

interface MyRepository {

    suspend fun getDriversRanking(): DriversList

    suspend fun getRacesInfo(): RaceInfo
}