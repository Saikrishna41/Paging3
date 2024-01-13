package com.techfortyone.beerappcompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "beertable")
data class BeerEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val tagline: String,
    val firstBrewed: String,
    val description: String,
    val imageUrl: String?
)
