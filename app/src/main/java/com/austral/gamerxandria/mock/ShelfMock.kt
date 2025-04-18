package com.austral.gamerxandria.mock

import com.austral.gamerxandria.model.Shelf

var userShelvesMock = listOf<Shelf>(
    Shelf("Favorites", listOf(1, 2, 3)),
    Shelf("Completed", listOf(2, 3)),
    Shelf("In progress", listOf(1, 3, 4, 8, 9, 10, 12)),
    Shelf("Backlog", listOf(2))
)

var searchShelvesMock = listOf<Shelf>(
    Shelf("Based on your library", listOf(1, 2, 3)),
    Shelf("Trending", listOf(2, 3)),
    Shelf("All-star", listOf(1, 3, 4, 8, 9, 10, 12))
)
