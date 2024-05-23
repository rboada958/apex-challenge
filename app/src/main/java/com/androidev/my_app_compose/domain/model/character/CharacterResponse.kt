package com.androidev.my_app_compose.domain.model.character

import com.squareup.moshi.Json

data class CharacterResponse(

	@Json(name = "results")
    val results: List<CharacterResultsItem?>? = null,

	@Json(name = "info")
    val info: Info? = null
)