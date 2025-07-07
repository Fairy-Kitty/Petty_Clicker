package com.example.clicker3.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.clicker3.data.entity.PurchasedImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedImageDao {
    @Query("SELECT * FROM purchased_images")
    fun getPurchasedImages(): Flow<List<PurchasedImageEntity>>

    @Insert
    suspend fun insertPurchasedImage(image: PurchasedImageEntity)

    @Query("UPDATE purchased_images SET customName = :customName WHERE id = :animalId")
    suspend fun updatePetName(animalId: Int, customName: String)

}
