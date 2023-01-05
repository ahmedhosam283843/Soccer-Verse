package com.example.soccerverse.leaguedetailscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.soccerverse.data.models.LeagueListEntry
import com.example.soccerverse.data.models.LeagueStandingEntry
import com.example.soccerverse.data.models.TeamListEntry
import com.example.soccerverse.data.remote.responses.leagueresponses.LeagueList
import com.example.soccerverse.data.remote.responses.standingresponses.LeagueStanding
import com.example.soccerverse.navigation.BottomNavItem
import com.example.soccerverse.navigation.Screen
import com.example.soccerverse.util.Resource
import java.util.*

@Composable
fun LeagueDetailScreen(
    dominantColor: Color,
    leagueId: Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    leagueImageSize: Dp = 120.dp,
    viewModel: LeagueDetailViewModel = hiltViewModel()
) {
    val leagueInfo = produceState<Resource<LeagueList>>(initialValue = Resource.Loading()) {
        value = viewModel.getLeagueById(leagueId)
        viewModel.getLeagueTeams(leagueId)
        viewModel.loadLeagueStanding(leagueId)
    }.value



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        LeagueDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        LeagueDetailStateWrapper(
            leagueInfo = leagueInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = leagueImageSize / 2f,
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
                    top = topPadding + leagueImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
            if (leagueInfo is Resource.Success) {
                leagueInfo.data?.response?.get(0)?.league?.logo.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = leagueInfo.data?.response?.get(0)?.league?.name,
                        modifier = Modifier
                            .size(leagueImageSize)
                            .offset(y = topPadding)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                }
            }

        }

        var appBarState by remember {
            mutableStateOf(BottomNavItem.Table.title)
        }

        DetailsSectionBar(
            onClick = {
                appBarState = it
            }, appBarState, modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 180.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp, top = 265.dp)
        ) {
            when (appBarState) {
                BottomNavItem.Table.title -> {
                    leagueInfo.data?.let { TableSection(leagueInfo = it) }
                }
                BottomNavItem.Teams.title -> {
                    leagueInfo.data?.let {
                        TeamsSection(
                            leagueInfo = it,
                            navController = navController
                        )
                    }
//                BottomNavItem.Matches.title -> {
//                    leagueInfo.data?.let { MatchesSection(leagueInfo = it) }
//                }
                }
            }
        }
    }
}


@Composable
fun LeagueDetailTopSection(
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
fun LeagueDetailStateWrapper(
    leagueInfo: Resource<LeagueList>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
) {
    when (leagueInfo) {
        is Resource.Success -> {
            LeagueDetailSection(
                leagueInfo = leagueInfo.data!!,
                modifier = modifier.offset(y = (-20).dp)
            )
        }

        is Resource.Error -> {
            Text(text = leagueInfo.message!!)
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
fun LeagueDetailSection(
    leagueInfo: LeagueList,
    modifier: Modifier = Modifier,
    viewModel: LeagueDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${leagueInfo.response[0].league.id} ${
                leagueInfo.response[0].league.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onSurface
        )


    }
}

@Composable
fun TeamsSection(
    leagueInfo: LeagueList,
    viewModel: LeagueDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val teamsList by remember {
        mutableStateOf(viewModel.teamList)
    }
    if (teamsList.value.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier.padding(bottom = 16.dp),
            columns = GridCells.Fixed(2),
            content = {
                teamsList.value.forEach {
                    item {
                        TeamCard(
                            team = it,
                            navController = navController,
                            leagueId = leagueInfo.response[0].league.id
                        )
                    }
                }
            })
    }

}

@Composable
fun TeamCard(
    team: TeamListEntry,
    viewModel: LeagueDetailViewModel = hiltViewModel(),
    navController: NavController,
    leagueId: Int
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    val dominantColor = remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        modifier = Modifier
            .padding(24.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable {
                navController.navigate(
                    "${Screen.TeamDetail.route}/${team.number}/${leagueId}/${dominantColor.value.toArgb()}"
                )
            }, contentAlignment = Alignment.Center
    ) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(team.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = team.teamName,
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            },
            onSuccess = { success ->
                viewModel.calculateDominantColor(success.result.drawable) { color ->
                    dominantColor.value = color
                }
            },

            modifier = Modifier
                .size(100.dp)
        )
    }
}

//@Composable
//fun MatchesSection(leagueInfo: LeagueList) {
//
//}

@Composable
fun TableSection(leagueInfo: LeagueList, viewModel: LeagueDetailViewModel = hiltViewModel()) {
    val leagueStanding by remember {
        mutableStateOf(viewModel.leagueStanding)
    }
    if (leagueStanding.value.isNotEmpty()) {
        DataTable(
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 12.dp),
            columnCount = 8,
            rowCount = leagueStanding.value.size + 1,
            cellContent = { columnIndex, rowIndex ->
                DataTableCell(
                    leagueStanding = leagueStanding.value,
                    columnIndex = columnIndex,
                    rowIndex = rowIndex
                )
            }
        )
    }
}

@Composable
fun DataTableCell(leagueStanding: List<LeagueStandingEntry>, columnIndex: Int, rowIndex: Int) {
    if (rowIndex == 0) {
        Text(
            text = when (columnIndex) {
                0 -> "Pos"
                1 -> "Team"
                2 -> "Pts"
                3 -> "GD"
                4 -> "P"
                5 -> "W"
                6 -> "D"
                7 -> "L"
                else -> ""
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(80.dp)
                .border(1.dp, Color.White)
                .padding(8.dp)
        )
    } else {
        Text(
            text = when (columnIndex) {
                0 -> leagueStanding[rowIndex - 1].teamPosition.toString()
                1 -> leagueStanding[rowIndex - 1].teamName
                2 -> leagueStanding[rowIndex - 1].teamPoints.toString()
                3 -> leagueStanding[rowIndex - 1].teamGoalDifference.toString()
                4 -> leagueStanding[rowIndex - 1].teamPlayedGames.toString()
                5 -> leagueStanding[rowIndex - 1].teamWon.toString()
                6 -> leagueStanding[rowIndex - 1].teamDraw.toString()
                7 -> leagueStanding[rowIndex - 1].teamLost.toString()
                else -> ""
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(80.dp)
                .border(1.dp, Color.White)
                .padding(8.dp),
            maxLines = 1
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsSectionBar(
    onClick: (String) -> Unit,
    sectionName: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Table,
//        BottomNavItem.Matches,
        BottomNavItem.Teams,

        )
    BottomAppBar(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp)),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        content = {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 15.sp
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.onSurface,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = item.title == sectionName,
                    onClick = { onClick(item.title) }
                )
            }

        }
    )


}