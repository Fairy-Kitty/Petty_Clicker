package com.example.clicker3.data.repository

import com.example.clicker3.data.dao.PurchasedImageDao
import com.example.clicker3.data.entity.PurchasedImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchasedImageRepository @Inject constructor(
    private val purchasedImageDao: PurchasedImageDao
) {
    val purchasedImages: Flow<List<PurchasedImageEntity>> = purchasedImageDao.getPurchasedImages()


    val regularAnimals = purchasedImages.map { images ->
        images.filter { it.animalType == "regular" }
    }

    val exoticAnimals = purchasedImages.map { images ->
        images.filter { it.animalType == "exotic" }
    }

    val fantasticAnimals = purchasedImages.map { images ->
        images.filter { it.animalType == "fantastic" }
    }

    val legendaryAnimals = purchasedImages.map { images ->
        images.filter { it.animalType == "legendary" }
    }

    suspend fun addPurchasedImage(entity: PurchasedImageEntity) {
        purchasedImageDao.insertPurchasedImage(entity)
    }
    suspend fun updatePetName(animalId: Int, customName: String) {
        purchasedImageDao.updatePetName(animalId, customName)
    }

}
