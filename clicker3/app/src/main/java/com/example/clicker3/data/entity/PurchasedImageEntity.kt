package com.example.clicker3.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchased_images")
data class PurchasedImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageName: String,
    val imageResId: Int,
    val price: Int,
    val animalType: String,
    val customName: String? = null
)

