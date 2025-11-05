package com.example.boxboxdemo.data.model

data class RaceInfo(
    val schedule: List<Schedule>
)

data class Schedule(
    val raceId: String,
    val circuitId: String,
    val isSprint: Boolean,
    val raceEndTime: Long,
    val raceName: String,
    val raceStartTime: Long,
    val raceState: String,
    val round: Long,
    val sessions: List<Session>,
    val podium: List<String>?
)

data class Session(
    val sessionId: String,
    val sessionType: String,
    val sessionName: String,
    val startTime: Long,
    val endTime: Long,
    val sessionState: String,
    val _id: String
)
