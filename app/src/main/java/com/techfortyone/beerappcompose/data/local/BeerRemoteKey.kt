package com.techfortyone.beerappcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "beerremotekeytable")
data class BeerRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val prevPage : Int?,
    val nextPage : Int?
)