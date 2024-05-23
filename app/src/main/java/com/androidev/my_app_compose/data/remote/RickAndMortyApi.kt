package com.androidev.my_app_compose.data.remote

import com.androidev.my_app_compose.domain.model.character.CharacterResponse
import com.androidev.my_app_compose.domain.model.character.CharacterResultsItem
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacterList(): ApiResponse<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: String): ApiResponse<CharacterResultsItem>
}