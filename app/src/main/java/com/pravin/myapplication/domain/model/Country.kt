package com.pravin.myapplication.domain.model

data class Country(
    val abbreviation: String,
    val capital: String,
    val currency: String,
    val name: String,
    val phone: String,
    val population: String?,
    val media: Media?,
    val id: Int
)


data class Media(val flag: String?, val emblem: String, val orthographic: String)
