package com.techfortyone.beerappcompose.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface BeerDao {

    @Upsert
    suspend fun updateAll(beers : List<BeerEntity>)

    @Query("SELECT * FROM beertable")
    fun pagingSource() : PagingSource<Int, BeerEntity>

    @Query("DELETE FROM beertable")
    suspend fun clearAll()

}