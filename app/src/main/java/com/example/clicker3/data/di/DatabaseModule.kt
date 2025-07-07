package com.example.clicker3.data.di

import android.content.Context
import androidx.room.Room
import com.example.clicker3.data.dao.ClickerDao
import com.example.clicker3.data.dao.PurchasedImageDao
import com.example.clicker3.data.database.ClickerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ClickerDatabase {
        return Room.databaseBuilder(
            context,
            ClickerDatabase::class.java,
            "clicker_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideClickerDao(database: ClickerDatabase): ClickerDao {
        return database.clickerDao()
    }

    @Provides
    fun providePurchasedImageDao(database: ClickerDatabase): PurchasedImageDao {
        return database.purchasedImageDao()
    }
}
