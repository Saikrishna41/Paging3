package com.techfortyone.beerappcompose.data.mappers

import com.techfortyone.beerappcompose.data.domain.Beer
import com.techfortyone.beerappcompose.data.local.BeerEntity
import com.techfortyone.beerappcompose.data.model.BeerDto

fun BeerDto.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name = name,
        tagline = tagline,
        firstBrewed = firstBrewed,
        description = description,
        imageUrl = imageUrl
    )
}

fun BeerEntity.toBeerModel() : Beer {
    return Beer(
        id = id,
        name = name,
        tagline = tagline,
        firstBrewed = firstBrewed,
        description = description,
        imageUrl = imageUrl
    )
}