package com.example.clicker3

import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.clicker3.data.entity.PurchasedImageEntity
import com.example.clicker3.data.repository.PurchasedImageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PurchasedImageRepositoryTest {
    private val mockDao = mock<com.example.clicker3.data.dao.PurchasedImageDao>()
    private val repository = PurchasedImageRepository(mockDao)

    @Test
    fun `addPurchasedImage вызывает insertPurchasedImage у DAO`() = runBlocking {
        val testImage = PurchasedImageEntity(
            id = 0,
            imageName = "cat",
            imageResId = 123,
            price = 100,
            animalType = "regular"
        )
        repository.addPurchasedImage(testImage)
        verify(mockDao).insertPurchasedImage(testImage)
    }
}