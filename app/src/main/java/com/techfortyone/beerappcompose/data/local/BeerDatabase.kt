package com.techfortyone.beerappcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [BeerEntity::class, BeerRemoteKey::class], version = 1, exportSchema = false
)
abstract class BeerDatabase : RoomDatabase() {

    abstract fun beerDao(): BeerDao

    abstract fun beerRemoteKeyDao() : BeerRemoteKeyDao
}