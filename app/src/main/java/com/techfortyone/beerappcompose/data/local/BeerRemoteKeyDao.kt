package com.techfortyone.beerappcompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface BeerRemoteKeyDao {

    @Query("SELECT * FROM beerremotekeytable WHERE id=:id")
    fun getRemoteKeys(id : Int): BeerRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRemoteKeys(remoteKey: List<BeerRemoteKey>)

    @Query("DELETE FROM beerremotekeytable")
    suspend fun deleteRemoteKeys()
}