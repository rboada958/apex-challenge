package com.androidev.my_app_compose.core.network.di

import com.androidev.my_app_compose.domain.repository.RickAndMortyRepository
import com.androidev.my_app_compose.domain.repository.RickAndMortyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsRickAndMortyRepository(
        rickAndMortyRepository: RickAndMortyRepositoryImpl
    ) : RickAndMortyRepository
}