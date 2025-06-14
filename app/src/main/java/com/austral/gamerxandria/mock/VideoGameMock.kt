package com.austral.gamerxandria.mock

import com.austral.gamerxandria.model.Company
import com.austral.gamerxandria.model.Cover
import com.austral.gamerxandria.model.Genre
import com.austral.gamerxandria.model.InvolvedCompany
import com.austral.gamerxandria.model.Platform
import com.austral.gamerxandria.model.VideoGame

var VideoGameMock = listOf(
    VideoGame(
        id = 1,
        name = "The Legend of Zelda: Breath of the Wild",
        cover = Cover(1, "https://i.pinimg.com/236x/d8/c9/d3/d8c9d3a2690ec2ca01880a2dc86a09c3.jpg"),
        first_release_date = 10,
        platforms = listOf(Platform(1, "Nintendo Switch"), Platform(1, "Nintendo Switch 2")),
        genres = listOf(Genre(1, "Action"), Genre(1, "Adventure")),
        involved_companies = listOf(InvolvedCompany(1, Company(1, "Nintendo"))),
        summary = "An open-world action-adventure game set in a post-apocalyptic world.",
    ),
    VideoGame(
        id = 2,
        name = "The Legend of Zelda: Tears of the Kingdom",
        cover = null,
        first_release_date = 10,
        platforms = listOf(Platform(1, "Nintendo Switch"), Platform(1, "Nintendo Switch 2")),
        genres = listOf(Genre(1, "Action"), Genre(1, "Adventure")),
        involved_companies = listOf(InvolvedCompany(1, Company(1, "Nintendo"))),
        summary = "An open-world action-adventure game set in a post-apocalyptic world.",
    )
)
