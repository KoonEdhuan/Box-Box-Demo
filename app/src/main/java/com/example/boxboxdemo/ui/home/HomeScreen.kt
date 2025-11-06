package com.example.boxboxdemo.ui.home

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.boxboxdemo.R
import com.example.boxboxdemo.data.model.BottomNavItem
import com.example.boxboxdemo.data.model.Driver
import com.example.boxboxdemo.data.model.Session
import com.example.boxboxdemo.ui.viewmodel.MyViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    val viewModel: MyViewModel = hiltViewModel()
    val context = LocalContext.current
    val customTabsIntent = CustomTabsIntent.Builder().build()

    val isLoading by viewModel.isLoading.observeAsState()
    val topRankDriver by viewModel.topRankDriver.observeAsState()
    val upcomingSession by viewModel.upcomingSession.observeAsState()
    val upcomingSessionStartTime by viewModel.upcomingSessionStartTime.observeAsState()
    val upcomingSessionDate by viewModel.upcomingSessionDate.observeAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val scaffoldColor = if (pagerState.currentPage == 0) {
        Color(0xFFF97700)
    } else {
        Black
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getDriversRanking()
    }

    Scaffold(
        containerColor = scaffoldColor,
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black)
                    .verticalScroll(rememberScrollState())
            ) {
                TopSection(topRankDriver, pagerState)

                MiddleSection(
                    onRaceClick = {
                        onNavigateToDetails()
                    },
                    onEducationClick = {
                        val url = "https://blog.boxbox.club/tagged/beginners-guide"
                        customTabsIntent.launchUrl(context, url.toUri())
                    },
                    onF1CardClick = {
                        val url = "https://www.instagram.com/boxbox_club/"
                        customTabsIntent.launchUrl(context, url.toUri())
                    },
                    upcomingSession = upcomingSession,
                    upcomingSessionStartTime = upcomingSessionStartTime,
                    upcomingSessionDate = upcomingSessionDate
                )
            }
            if (isLoading == true) {
                LoadingIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Blue,
                    polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
                )
            }
        }
    }
}

@Composable
fun TopSection(topRankDriver: Driver?, pagerState: androidx.compose.foundation.pager.PagerState) {

    LaunchedEffect(Unit) {
        while(true) {
            delay(3000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF97700),
                                    Color(0xFFF97700),
                                    Black
                                )
                            )
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (pagerState.currentPage == 0) {

                            DriverInfo(topRankDriver)
                        } else {
                            CommunityCard()
                        }
                    }
                }
            }

            GetProButton()
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            ,horizontalArrangement = Arrangement.Center)
        {
            repeat(2) { iteration ->
                val color = if (pagerState.currentPage == iteration) White else Gray
                val width by animateDpAsState(targetValue = if (pagerState.currentPage == iteration) 32.dp else 6.dp)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Composable
fun GetProButton() {
    Button(
        modifier = Modifier
            .padding(16.dp),
        onClick = {  },
        colors = ButtonDefaults.buttonColors(containerColor = White.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(Icons.Default.Diamond, contentDescription = "Get Pro", tint = White)
        Spacer(Modifier.width(8.dp))
        Text("Get Pro", color = White)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DriverInfo(info: Driver?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            info?.firstName.toString(),
            fontSize = 80.sp,
            color = White.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(id = R.drawable.graph),
                contentDescription = "Position",
                tint = Yellow,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = info?.position.toString(),
                fontSize = 24.sp, color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(2.dp))

            Text(
                "Pos",
                fontSize = 14.sp,
                color = White,
                style = MaterialTheme.typography.labelSmallEmphasized
            )

            Spacer(Modifier.width(16.dp))

            Icon(
                painterResource(id = R.drawable.bullseye),
                contentDescription = "Position",
                tint = Yellow,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(2.dp))

            Text(
                info?.wins.toString(),
                fontSize = 24.sp, color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(2.dp))

            Text(
                "Wins",
                fontSize = 14.sp,
                color = White,
                style = MaterialTheme.typography.labelSmallEmphasized
            )
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = info?.points.toString(),
                style = TextStyle(
                    brush = Brush.verticalGradient(
                        colors = listOf(White,Color(0xFFF97700))
                    ),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Light
                )
            )
            Spacer(Modifier.width(2.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF97700)),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("PTS", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MiddleSection(
    upcomingSession: Session?,
    upcomingSessionStartTime: String?,
    upcomingSessionDate: String?,
    onRaceClick: () -> Unit,
    onEducationClick: () -> Unit,
    onF1CardClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RaceInfoCard(
                modifier = Modifier.weight(1f),
                upcomingSession = upcomingSession,
                upcomingSessionStartTime = upcomingSessionStartTime,
                upcomingSessionDate = upcomingSessionDate,
                onRaceClick = { onRaceClick() }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                DistanceCard()
                EducationCard(
                    onEducationClick = { onEducationClick() }
                )
            }
        }
        F1GameCard(
            onF1CardClick = { onF1CardClick() }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RaceInfoCard(
    modifier: Modifier = Modifier,
    upcomingSessionStartTime: String?,
    upcomingSessionDate: String?,
    upcomingSession: Session?,
    onRaceClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(
                enabled = true,
                onClick = { onRaceClick() }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C5A3A))
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier=Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Map, contentDescription = "Track", tint = White, modifier = Modifier.size(24.dp))
            }

            Column {
                Text(
                    upcomingSession?.sessionName ?: "",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = White, modifier = Modifier.size(16.dp))

                    Spacer(Modifier.width(4.dp))

                    Text(
                        upcomingSessionDate ?: "",
                        color = White,
                        fontFamily = FontFamily.SansSerif,
                        style = MaterialTheme.typography.titleMediumEmphasized,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    upcomingSessionStartTime ?: "",
                    color = Color(0xFF66BB6A),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
        }
    }
}

@Composable
fun DistanceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Red)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Route, contentDescription = "Distance", tint = White)
            Spacer(Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "7015.3",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    "km",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EducationCard(
    onEducationClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = { onEducationClick() }
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, contentDescription = "Education", tint = White)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        "Formula 1",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmallEmphasized
                    )
                    Text(
                        "Education",
                        color = White,
                        style = MaterialTheme.typography.bodyLargeEmphasized,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                contentDescription = "External URL",
                tint = White,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun F1GameCard(
    onF1CardClick: () -> Unit
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.lewis_hamilton_img),
            contentDescription = "F1 25 Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true, onClick = onF1CardClick)
                .height(400.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "F1 25",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.TopCenter),
                    color = White,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Instagram",
                    tint = White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
        }
    }
}


@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Black) {
        val items = listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Schedule", Icons.Default.CalendarToday),
            BottomNavItem("Standings", Icons.Default.EmojiEvents),
            BottomNavItem("News", Icons.Default.Public),
            BottomNavItem("Profile", Icons.Default.Person)
        )
        var selectedItem by remember { mutableStateOf("Home") }
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {  },
                selected = selectedItem == item.title,
                onClick = { selectedItem = item.title },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = White,
                    unselectedIconColor = Gray,
                    selectedTextColor = White,
                    unselectedTextColor = Gray,
                    indicatorColor = Color(0xFF333333)
                )
            )
        }
    }
}

@Composable
fun CommunityCard() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(400.dp)
            .background(Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "WE ARE",
                        color = Color.LightGray,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "MORE THAN",
                        color = Color(0xFF4A90E2),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "JUST AN APP",
                        color = Black,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                Color(0xFFAFFF7A),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "JOIN OUR F1 COMMUNITY",
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAFFF7A)),
                modifier = Modifier.size(width = 200.dp, height = 60.dp)
            ) {
                Text(
                    "Follow Us",
                    color = Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToDetails = {})
}