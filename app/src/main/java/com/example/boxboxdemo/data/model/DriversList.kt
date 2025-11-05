package com.example.boxboxdemo.data.model

data class DriversList(
    val drivers: List<Driver>,
)

data class Driver(
    val driverId: String,
    val podiums: Long,
    val points: Long,
    val poles: Long,
    val position: Long,
    val teamId: String,
    val wins: Long,
    val firstName: String,
    val lastName: String,
    val driverCode: String,
    val teamName: String,
    val racingNumber: Long,
)
