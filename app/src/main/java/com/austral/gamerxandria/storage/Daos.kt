package com.austral.gamerxandria.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShelfDao {
    @Insert
    suspend fun insert(shelf: Shelf)

    @Update
    suspend fun update(shelf: Shelf)

    @Delete
    suspend fun delete(shelf: Shelf)

    @Query("SELECT * FROM shelves")
    fun getAllShelves(): LiveData<List<Shelf>>

    @Query("SELECT * FROM shelves WHERE name = :shelfName")
    suspend fun getShelfByName(shelfName: String): Shelf?
}

@Dao
interface ShelfVideoGameDao {
    @Insert
    suspend fun insert(shelfVideoGame: ShelfVideoGame)

    @Delete
    suspend fun delete(shelfVideoGame: ShelfVideoGame)

    @Query("SELECT * FROM videoGameIds WHERE shelfName = :shelfName")
    fun getVideoGameIdsByShelfName(shelfName: String): LiveData<List<ShelfVideoGame>>

    @Query("SELECT * FROM videoGameIds WHERE shelfName = :shelfName")
    suspend fun getVideoGameIdsByShelfNameSync(shelfName: String): List<ShelfVideoGame>

    @Query("DELETE FROM videoGameIds WHERE shelfName = :shelfName AND videoGameId = :gameId")
    suspend fun deleteVideoGameFromShelf(shelfName: String, gameId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM videoGameIds WHERE shelfName = :shelfName AND videoGameId = :gameId)")
    suspend fun isGameInShelf(shelfName: String, gameId: Int): Boolean
}
