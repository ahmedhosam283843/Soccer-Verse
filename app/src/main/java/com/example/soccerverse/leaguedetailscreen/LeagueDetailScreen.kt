package com.example.soccerverse.leaguedetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.soccerverse.data.remote.responses.LeagueList
import com.example.soccerverse.util.Resource
import java.util.*

@Composable
fun LeagueDetailScreen(
    dominantColor: Color,
    leagueId: Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    leagueImageSize: Dp = 200.dp,
    viewModel: LeagueDetailViewModel = hiltViewModel()
) {
    val leagueInfo = produceState<Resource<LeagueList>>(initialValue = Resource.Loading()) {
        value = viewModel.getLeagueById(leagueId)
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
                    top = topPadding + leagueImageSize / 2f,
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
    modifier: Modifier = Modifier
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
            fontSize = 30.sp,
            color = MaterialTheme.colors.onSurface
        )
//        PokemonTypeSection(types = pokemonInfo.types)
//        PokemonDetailDataSection(
//            pokemonWeight = pokemonInfo.weight,
//            pokemonHeight = pokemonInfo.height
//        )
//        PokemonBaseStats(pokemonInfo = pokemonInfo)
    }
}