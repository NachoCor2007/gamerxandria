package com.austral.gamerxandria.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "shelves")
data class Shelf(
    @PrimaryKey
    val name: String,
)

@Entity(
    tableName = "videoGameIds",
    foreignKeys = [
        ForeignKey(
            entity = Shelf::class,
            parentColumns = ["name"],
            childColumns = ["shelfName"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ShelfVideoGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val shelfName: String,
    val videoGameId: Int,
)
