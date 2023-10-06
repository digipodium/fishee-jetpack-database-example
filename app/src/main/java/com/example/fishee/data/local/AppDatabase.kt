package com.example.fishee.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fishee.data.local.models.Fish
import com.example.fishee.data.local.models.User

@Database(
    entities = [User::class, Fish::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun fishDao(): FishDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "fishee_db"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            return instance
        }
    }

}