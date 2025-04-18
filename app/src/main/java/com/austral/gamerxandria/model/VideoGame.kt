package com.austral.gamerxandria.model

data class RetrievedVideoGame(
    val id: Int,
    val name: String,
    val summary: String,
    val cover: Int,
    val first_release_date: Long,
    val aggregated_rating: Double,
    val genres: List<Int>,
    val platforms: List<Int>,
    val involved_companies: String,
)

data class ParsedVideoGame(
    val id: Int,
    val name: String,
    val summary: String,
    val cover: Cover,
    val first_release_date: Long,
    val aggregated_rating: Double,
    val genres: List<Genre>,
    val platforms: List<Platform>,
    val involved_companies: String,
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
