package com.techfortyone.beerappcompose.di.modules

import com.techfortyone.beerappcompose.data.network.BeerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder().baseUrl(BeerApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()


    @Provides
    @Singleton
    fun provideBeerApi(retrofit: Retrofit) = retrofit.create(BeerApi::class.java)


}