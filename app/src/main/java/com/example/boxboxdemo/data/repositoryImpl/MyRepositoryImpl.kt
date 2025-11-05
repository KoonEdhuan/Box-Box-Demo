package com.example.boxboxdemo.data.repositoryImpl

import com.example.boxboxdemo.data.model.DriversList
import com.example.boxboxdemo.data.model.RaceInfo
import com.example.boxboxdemo.data.repository.MyRepository
import com.example.boxboxdemo.data.service.MyApiService
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val service: MyApiService
) : MyRepository {

    override suspend fun getDriversRanking(): DriversList {
        return service.getDriversRanking()
    }

    override suspend fun getRacesInfo(): RaceInfo {
        return service.getRacesInfo()
    }
}