package com.techfortyone.beerappcompose.data.network

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.techfortyone.beerappcompose.data.local.BeerDatabase
import com.techfortyone.beerappcompose.data.local.BeerEntity
import com.techfortyone.beerappcompose.data.local.BeerRemoteKey
import com.techfortyone.beerappcompose.data.mappers.toBeerEntity
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDatabase: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

    @WorkerThread
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {
            val currPage = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("TAGSS", "refresh called")

                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    Log.d("TAGSS", "prepend called")
//                    val firstItem = getRemoteKeyForFirstItem(state)
//
//                    if (firstItem == null) {
//                        1
//                    }
//                    else {
//                        (firstItem.id / state.config.pageSize - 1)
//                    }
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKey?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    Log.d("TAGSS", "append called")

                    //last item in current list
                    val remoteKey = getRemoteKeyForLastItem(state)

                    val nextPage = remoteKey?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                    nextPage
                    //no last item it must be first page
//                    if (lastItem == null) {
//                        1
//                    }
//                    //calculate next page, id are ascending in this case, id = 80 pagesize = 20 == 80 / 20 = 4 + 1 = 5 os next page
//                    else {
//                        (lastItem.id / state.config.pageSize + 1)
//                    }
                }
            }
            delay(5000)
            val beers = beerApi.getBeers(currPage, 40)
            val endPaginationReached = beers.isEmpty()
            val prevPage = if (currPage == 1) null else currPage - 1
            val nextPage = if (endPaginationReached) null else currPage + 1

            Log.d("TAGSS", "curPage is $currPage")
            Log.d("TAGSS", "prevPage is $prevPage")
            Log.d("TAGSS", "nextPage is $nextPage")

            //with transaction because multiple transactions are being executed, either all calls succeed or non
            beerDatabase.withTransaction {//if refresh clear cache, to insert new entries
                if (loadType == LoadType.REFRESH) {
                    beerDatabase.beerDao().clearAll()
                    beerDatabase.beerRemoteKeyDao().deleteRemoteKeys()
                }
                val beerEntities = beers.map {
                    it.toBeerEntity()
                }
                val keys = beerEntities.map {
                    BeerRemoteKey(
                        id = it.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                beerDatabase.beerDao().updateAll(beerEntities)
                beerDatabase.beerRemoteKeyDao().addRemoteKeys(keys)
            }
            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, BeerEntity>): BeerRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { beerEntity ->
                beerDatabase.beerRemoteKeyDao().getRemoteKeys(beerEntity.id)
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, BeerEntity>): BeerRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { beerEntity ->
                beerDatabase.beerRemoteKeyDao().getRemoteKeys(beerEntity.id)
            }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, BeerEntity>): BeerRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                beerDatabase.beerRemoteKeyDao().getRemoteKeys(id)
            }
        }
    }
}

