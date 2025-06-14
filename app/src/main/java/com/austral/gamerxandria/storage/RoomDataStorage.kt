package com.austral.gamerxandria.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shelf::class, ShelfVideoGame::class], version = 1)
abstract class GamerxandriaDatabase : RoomDatabase() {
    abstract fun shelfDao(): ShelfDao
    abstract fun videoGameIdDao(): ShelfVideoGameDao

    companion object {
        @Volatile
        private var INSTANCE: GamerxandriaDatabase? = null

        fun getDatabase(context: Context): GamerxandriaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamerxandriaDatabase::class.java,
                    "gamerxandria_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
