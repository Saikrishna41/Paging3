package com.techfortyone.beerappcompose.di.modules

import android.content.Context
import androidx.room.Room
import com.techfortyone.beerappcompose.data.local.BeerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, BeerDatabase::class.java,
        "Beer.db"
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun providesBeerDao(database: BeerDatabase) = database.beerDao()

    @Provides
    @Singleton
    fun provideBeerRemoteKeyDao(database: BeerDatabase) = database.beerRemoteKeyDao()
}