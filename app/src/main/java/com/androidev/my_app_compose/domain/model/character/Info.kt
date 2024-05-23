package com.androidev.my_app_compose.domain.model.character

import com.squareup.moshi.Json

data class Info(

    @Json(name = "next")
    val next: String? = null,

    @Json(name = "pages")
    val pages: Int? = null,

    @Json(name = "prev")
    val prev: Any? = null,

    @Json(name = "count")
    val count: Int? = null
)