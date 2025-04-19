package com.austral.gamerxandria.model

data class RetrievedVideoGame(
    val id: Int,
    val name: String,
    val cover: Int,
    val first_release_date: Long,
    val aggregated_rating: Double,
    val platforms: List<Int>,
    val genres: List<Int>,
    val involved_companies: List<Int>,
    val summary: String,
)

data class VideoGame(
    val id: Int,
    val name: String,
    val cover: Cover,
    val first_release_date: String,
    val aggregated_rating: Double,
    val platforms: List<Platform>,
    val genres: List<Genre>,
    val involved_companies: List<Company>,
    val summary: String,
)

data class Cover(
    val url: String,
)

data class Genre(
    val name: String
)

data class Platform(
    val name: String
)

data class Company(
    val name: String
)
