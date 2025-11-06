package com.example.boxboxdemo.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boxboxdemo.data.model.RaceCountdown
import com.example.boxboxdemo.ui.theme.DarkGreen
import com.example.boxboxdemo.ui.theme.LightGreen
import com.example.boxboxdemo.ui.viewmodel.MyViewModel
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit,
) {
    val viewModel: MyViewModel = hiltViewModel()

    val isLoading by viewModel.isLoading.observeAsState()
    val upcomingRace by viewModel.nextUpcomingRace.observeAsState()
    val upcomingSession by viewModel.upcomingSession.observeAsState()
    val upcomingRaceDateRange by viewModel.upcomingRaceDateRange.observeAsState()
    var countdown by remember { mutableStateOf<RaceCountdown?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getRacesInfo()
    }

    LaunchedEffect(key1 = upcomingSession) {
        upcomingSession?.let { session ->
            val instant = Instant.ofEpochSecond(session.startTime)
            val sessionTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
            while (true) {
                val now = ZonedDateTime.now()
                val duration = Duration.between(now, sessionTime)
                countdown = if (duration.isNegative || duration.isZero) {
                    RaceCountdown(0, 0, 0)
                } else {
                    RaceCountdown(duration.toDays(), duration.toHours() % 24, duration.toMinutes() % 60)
                }
                delay(1000)
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }
    Scaffold(
        containerColor = DarkGreen,
        bottomBar = {
            NavigationBar(containerColor = Black) {}
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkGreen, Black, Black)
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = "Upcoming race",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Round ${upcomingRace?.round}", color = White)
                        Text(
                            text = upcomingRace?.raceName ?: "",
                            color = White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val circuitId = upcomingRace?.circuitId ?: ""
                        val circuitName = circuitId
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ")
                            {
                                it.replaceFirstChar(Char::titlecase)
                            }.trim()
                        Text(text = circuitName, color = LightGreen)
                        Text(text = upcomingRaceDateRange ?: "", color = White)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "${upcomingSession?.sessionName} Starts in", color = White)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(

                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CountdownUnit(time = countdown?.days?.toString()?.padStart(2, '0') ?: "00", unit = "Days")
                        CountdownUnit(time = countdown?.hours?.toString()?.padStart(2, '0') ?: "00", unit = "Hours")
                        CountdownUnit(time = countdown?.minutes?.toString()?.padStart(2, '0') ?: "00", unit = "Minutes")
                    }


                    Spacer(modifier = Modifier.height(32.dp))
                }

                InfoSection(
                    title = "São Paulo Circuit",
                    description = "Bahrain International circuit is located in Sakhir, Bahrain and it was designed by German architect Hermann Tilke. It was built on the site of a former camel farm, in Sakhir. It measures 5.412 km, has 15 corners and 3 DRS Zones. The Grand Prix have 57 laps. This circuit has 6 alternative layouts."
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoSection(
                    title = "Circuit Facts",
                    description = "His brother Arthur Leclerc is currently set to race for DAMS in the 2023 F2 Championship.\n\nHe is not related to Édouard Leclerc, the founder of a French supermarket chain"
                )
            }

            if (isLoading == true) {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = LightGreen,
                    polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
                )
            }
        }
    }
}

@Composable
fun CountdownUnit(time: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = time, color = LightGreen, fontSize = 36.sp, fontWeight = FontWeight.Normal)
        Text(text = unit, color = White)
    }
}

@Composable
fun InfoSection(title: String, description: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            color = White,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen { }
}
