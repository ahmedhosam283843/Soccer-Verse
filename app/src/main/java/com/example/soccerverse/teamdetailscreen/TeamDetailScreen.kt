package com.example.soccerverse.teamdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.soccerverse.data.remote.responses.leagueteamsresponse.LeagueTeam
import com.example.soccerverse.util.Resource
import java.util.*

@Composable
fun TeamDetailScreen(
    teamId: Int,
    leagueId: Int,
    dominantColor: Color,
    navController: NavController,
    topPadding: Dp = 20.dp,
    teamImageSize: Dp = 200.dp,
    viewModel: TeamDetailViewModel = hiltViewModel()
) {
    val teamInfo = produceState<Resource<LeagueTeam>>(initialValue = Resource.Loading()) {
        value = viewModel.getTeamById(teamId, leagueId)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        TeamDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        TeamDetailStateWrapper(
            teamInfo = teamInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = teamImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + teamImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            dominantColor = dominantColor,
        )
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
            if (teamInfo is Resource.Success) {
                teamInfo.data?.response?.get(0)?.team?.logo?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = teamInfo.data.response[0].team.name,
                        modifier = androidx.compose.ui.Modifier
                            .size(teamImageSize)
                            .offset(y = topPadding)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                }
            }

        }

    }
}


@Composable
fun TeamDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.background(
            Brush.verticalGradient(listOf(Color.Black, Color.Transparent))
        )
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun TeamDetailStateWrapper(
    teamInfo: Resource<LeagueTeam>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    dominantColor: Color
) {
    when (teamInfo) {
        is Resource.Success -> {
            TeamDetailSection(
                teamInfo = teamInfo.data!!,
                modifier = modifier.offset(y = (-20).dp),
                dominantColor = dominantColor
            )
        }

        is Resource.Error -> {
            Text(text = teamInfo.message!!)
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun TeamDetailSection(
    teamInfo: LeagueTeam,
    modifier: Modifier = Modifier,
    viewModel: TeamDetailViewModel = hiltViewModel(),
    dominantColor: Color
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .fillMaxSize()
            .offset(y = 140.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${teamInfo.response[0].team.id} ${
                teamInfo.response[0].team.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))
        TeamInfoBubbleSection(
            infos = listOf(
                "Founded: ${teamInfo.response[0].team.founded}",
                "Stadium: ${teamInfo.response[0].venue.name}",
                "Capacity: ${teamInfo.response[0].venue.capacity}",

                ), dominantColor = dominantColor
        )
        TeamStadiumImageSection(teamInfo = teamInfo)

    }
}

@Composable
fun TeamStadiumImageSection(teamInfo: LeagueTeam) {
    teamInfo.response[0].venue.image.let {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),
            contentDescription = teamInfo.response[0].venue.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(50.dp))
        )
    }
}


@Composable
fun TeamInfoBubbleSection(infos: List<String>, dominantColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (info in infos) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(dominantColor)
                    .height(50.dp)
            ) {
                Text(
                    text = info.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, color = contentColorFor(backgroundColor = dominantColor), fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

            }
        }
    }

}