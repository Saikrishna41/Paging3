package com.techfortyone.beerappcompose.data.model

import com.google.gson.annotations.SerializedName

data class BeerDto(
    val id: Int,
    val name: String,
    val tagline: String,
    @SerializedName("first_brewed")
    val firstBrewed: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String
)
