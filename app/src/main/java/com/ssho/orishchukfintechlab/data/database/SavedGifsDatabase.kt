package com.ssho.orishchukfintechlab.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssho.orishchukfintechlab.data.database.entities.ImageDataEntity

private const val DATABASE_NAME = "saved-gifs-database.db"

@Database(entities = [ImageDataEntity::class], version = 1, exportSchema = false)
abstract class SavedGifsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: SavedGifsDatabase? = null

        fun getSavedGifsDatabase(context: Context): SavedGifsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    SavedGifsDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun savedGifsDao(): SavedGifsDao
}