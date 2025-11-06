package com.example.boxboxdemo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boxboxdemo.data.model.Driver
import com.example.boxboxdemo.data.model.Schedule
import com.example.boxboxdemo.data.model.Session
import com.example.boxboxdemo.data.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {

    private val _topRankDriver = MutableLiveData<Driver?>()
    val topRankDriver: LiveData<Driver?> = _topRankDriver

    private val _nextUpcomingRace = MutableLiveData<Schedule>()
    val nextUpcomingRace: LiveData<Schedule> = _nextUpcomingRace

    private val _upcomingSession = MutableLiveData<Session>()
    val upcomingSession: LiveData<Session> = _upcomingSession

    private val _upcomingSessionDate = MutableLiveData<String>()
    val upcomingSessionDate: LiveData<String> = _upcomingSessionDate

    private val _upcomingRaceDateRange = MutableLiveData<String>()
    val upcomingRaceDateRange: LiveData<String> = _upcomingRaceDateRange

    private val _upcomingSessionStartTime = MutableLiveData<String>()
    val upcomingSessionStartTime: LiveData<String> = _upcomingSessionStartTime

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDriversRanking() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val drivers = repository.getDriversRanking()
                _topRankDriver.postValue( drivers.drivers.find {
                    it.position.toInt() == 1
                })
                getRacesInfo()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getRacesInfo() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val raceInfo = repository.getRacesInfo()
                val upcomingRace = findNextUpcomingRace(raceInfo.schedule)
                _nextUpcomingRace.postValue(upcomingRace)

                upcomingRace?.let { it ->
                    val nextSession = findNextUpcomingSession(it)
                    _upcomingSession.postValue(nextSession)
                    nextSession?.let {
                        val firstSession = findSessionByName(upcomingRace, "Practice 1")
                        val lastSession = findSessionByName(upcomingRace, "Race")
                        if (firstSession != null && lastSession != null) {
                            calculateRaceDateRange(firstSession.startTime, lastSession.startTime)
                        }
                        calculateRaceTime(it.startTime)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun findNextUpcomingRace(races: List<Schedule>): Schedule? {
        val currentTime = System.currentTimeMillis() / 1000
        return races.firstOrNull {
            it.raceEndTime > currentTime
        }
    }

    private fun findNextUpcomingSession(race: Schedule): Session? {
        val sortedSessions = race.sessions.sortedBy { it.startTime }

        val currentTime = System.currentTimeMillis() / 1000

        return sortedSessions.firstOrNull { it.endTime > currentTime }
    }

    private fun findSessionByName(race: Schedule, sessionName: String): Session? {
        return race.sessions.firstOrNull { it.sessionName == sessionName }
    }

    private fun calculateRaceDateRange(firstSessionTime: Long, lastSessionTime: Long) {
        val firstDate = Date(firstSessionTime * 1000)
        val lastDate = Date(lastSessionTime * 1000)

        val sdfDay = SimpleDateFormat("d")
        sdfDay.timeZone = TimeZone.getDefault()

        val sdfDayAndMonth = SimpleDateFormat("d MMMM")
        sdfDayAndMonth.timeZone = TimeZone.getDefault()

        val dateRange = "${sdfDay.format(firstDate)} - ${sdfDayAndMonth.format(lastDate)}"
        _upcomingRaceDateRange.postValue(dateRange)
    }

    private fun calculateRaceTime(raceTime: Long) {

         val date = Date(raceTime * 1000)

         val sdfDate = SimpleDateFormat("dd, EEEE")
         sdfDate.timeZone = TimeZone.getDefault()
         _upcomingSessionDate.postValue(sdfDate.format(date))

         val sdfTime = SimpleDateFormat("hh:mm a")
         sdfTime.timeZone = TimeZone.getDefault()
        _upcomingSessionStartTime.postValue(sdfTime.format(date).uppercase(Locale.ROOT))
    }
}