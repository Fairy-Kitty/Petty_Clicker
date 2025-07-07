package com.example.clicker3.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clicker_data")
data class ClickerEntity(
    @PrimaryKey val id: Int = 1,
    val clickCount: Int = 0
)
