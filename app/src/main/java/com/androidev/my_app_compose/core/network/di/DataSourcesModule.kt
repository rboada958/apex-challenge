package com.androidev.my_app_compose.core.network.di

import com.androidev.my_app_compose.domain.datasource.RickAndMortyDataSource
import com.androidev.my_app_compose.domain.datasource.RickAndMortyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {

    @Binds
    fun bindsRickAndMortyRemoteDataSource(
        dataSource: RickAndMortyDataSourceImpl
    ) : RickAndMortyDataSource
}