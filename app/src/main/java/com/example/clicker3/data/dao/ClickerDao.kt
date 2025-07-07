package com.example.clicker3.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.clicker3.data.entity.ClickerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClickerDao {
    @Query("SELECT * FROM clicker_data WHERE id = 1")
    fun getClickerData(): Flow<ClickerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveClickerData(clickerEntity: ClickerEntity)
}
