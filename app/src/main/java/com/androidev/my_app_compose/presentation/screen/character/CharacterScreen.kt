package com.androidev.my_app_compose.presentation.screen.character

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.androidev.my_app_compose.core.commons.Extensions.capitalizeFirstLetter
import com.androidev.my_app_compose.core.commons.UiState
import com.androidev.my_app_compose.domain.model.character.CharacterResultsItem
import kotlin.random.Random

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterScreen(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    val id = navController.currentBackStackEntry?.arguments?.getString("id") ?: ""
    Character(viewModel, id, navController, sharedTransitionScope, animatedVisibilityScope)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun Character(
    viewModel: CharacterViewModel,
    id: String,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Rick and Morty")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            when (state) {
                UiState.Loading -> {
                    viewModel.getCharacter(id)
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
                        (state as UiState.Success<*>).response as CharacterResultsItem
                    CharacterDetails(
                        characterResponse,
                        sharedTransitionScope,
                        animatedVisibilityScope
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterDetails(
    characterResponse: CharacterResultsItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val gender = characterResponse.gender
    val status = characterResponse.status
    val species = characterResponse.species
    val origin = characterResponse.origin?.name
    val location = characterResponse.location?.name
    val episodes = characterResponse.episode?.size
    val image = characterResponse.image
    val name = characterResponse.name
    val uid = characterResponse.id

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            name?.let {
                Text(
                    text = "${it.capitalizeFirstLetter()} - N.º 000$uid",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Image(
                painter = rememberImagePainter(image),
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = "image-${characterResponse.id}"),
                        animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Status",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        status?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Species",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        species?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Gender",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        gender?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Origin",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        origin?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Location",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        location?.let {
                            Text(
                                text = it.capitalizeFirstLetter(),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(60.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = randomColor()
                        )
                    ) {
                        Text(
                            text = "Episodes",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        episodes?.let {
                            Text(
                                text = "$it",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}