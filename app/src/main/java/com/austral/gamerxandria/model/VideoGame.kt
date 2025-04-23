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
    val first_release_date: Long,
    val aggregated_rating: Double,
    val platforms: List<Platform>,
    val genres: List<Genre>,
    val involved_companies: List<InvolvedCompany>,
    val summary: String,
)

data class Cover(
    val id: Int,
    val url: String
)

data class Platform(
    val id: Int,
    val name: String
)

data class Genre(
    val id: Int,
    val name: String
)

data class InvolvedCompany(
    val id: Int,
    val company: Company
)

data class Company(
    val id: Int,
    val name: String
)
