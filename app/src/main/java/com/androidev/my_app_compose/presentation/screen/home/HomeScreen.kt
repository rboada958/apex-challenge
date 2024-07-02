package com.androidev.my_app_compose.presentation.screen.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.androidev.my_app_compose.core.commons.Extensions.capitalizeFirstLetter
import com.androidev.my_app_compose.core.commons.UiState
import com.androidev.my_app_compose.domain.model.character.CharacterResponse
import com.androidev.my_app_compose.domain.model.character.CharacterResultsItem
import com.androidev.my_app_compose.presentation.navigation.characterNavigationRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Home(navController, sharedTransitionScope, animatedVisibilityScope, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun Home(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick and Morty") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (state) {
                UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }

                is UiState.Error -> {
                    val msg = (state as UiState.Error).msg
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success<*> -> {
                    val characterResponse =
                        (state as UiState.Success<*>).response as CharacterResponse
                    val characterResponseList = characterResponse.results
                    characterResponseList?.let {
                        CharacterCard(
                            it,
                            navController,
                            sharedTransitionScope,
                            animatedVisibilityScope
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterCard(
    resultsItem: List<CharacterResultsItem?>,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(start = 16.dp, top = 35.dp, bottom = 48.dp)
        ) {
            items(resultsItem.size) { index ->
                val item = resultsItem[index]
                if (item != null) {
                    val image = item.image
                    Column(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .size(width = 160.dp, height = 140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val id = index + 1
                                    navController.navigate("$characterNavigationRoute/$id")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFD9D9D9)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = rememberImagePainter(image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillMaxWidth()
                                        .sharedElement(
                                            rememberSharedContentState(key = "image-${item.id}"),
                                            animatedVisibilityScope
                                        )
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        item.name?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier.padding(bottom = 18.dp),
                                lineHeight = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
