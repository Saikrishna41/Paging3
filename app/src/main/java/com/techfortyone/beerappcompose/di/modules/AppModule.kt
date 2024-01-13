package com.techfortyone.beerappcompose.di.modules

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.techfortyone.beerappcompose.data.local.BeerDatabase
import com.techfortyone.beerappcompose.data.local.BeerEntity
import com.techfortyone.beerappcompose.data.network.BeerApi
import com.techfortyone.beerappcompose.data.network.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(beerDatabase: BeerDatabase, beerApi: BeerApi): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 40),
            remoteMediator = BeerRemoteMediator(
                beerDatabase,
                beerApi
            ),
            pagingSourceFactory = {
                beerDatabase.beerDao().pagingSource()
            }
        )
    }
}