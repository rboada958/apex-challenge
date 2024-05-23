package com.androidev.my_app_compose.domain.model.character

import com.squareup.moshi.Json

data class Location(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "url")
    val url: String? = null
)