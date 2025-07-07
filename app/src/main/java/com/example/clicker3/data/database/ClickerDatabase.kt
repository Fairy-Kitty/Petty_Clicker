package com.example.clicker3.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.clicker3.data.dao.ClickerDao
import com.example.clicker3.data.dao.PurchasedImageDao
import com.example.clicker3.data.entity.ClickerEntity
import com.example.clicker3.data.entity.PurchasedImageEntity

@Database(entities = [ClickerEntity::class, PurchasedImageEntity::class], version = 5, exportSchema = false)
abstract class ClickerDatabase : RoomDatabase() {
    abstract fun clickerDao(): ClickerDao
    abstract fun purchasedImageDao(): PurchasedImageDao

    companion object {
        @Volatile
        private var INSTANCE: ClickerDatabase? = null

        fun getDatabase(context: Context): ClickerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClickerDatabase::class.java,
                    "clicker_database"
                )

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
