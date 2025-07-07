package com.example.clicker3.data.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker3.Animal
import com.example.clicker3.data.entity.PurchasedImageEntity
import com.example.clicker3.data.repository.ClickerRepository
import com.example.clicker3.data.repository.PurchasedImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val clickerRepository: ClickerRepository,
    private val purchasedImageRepository: PurchasedImageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val clickCount = clickerRepository.clickCount
    val purchasedImages = purchasedImageRepository.purchasedImages
    val regularAnimals = purchasedImageRepository.regularAnimals
    val exoticAnimals = purchasedImageRepository.exoticAnimals
    val fantasticAnimals = purchasedImageRepository.fantasticAnimals
    val legendaryAnimals = purchasedImageRepository.legendaryAnimals


    private val _shownAnimalId = MutableStateFlow(savedStateHandle.get<Int?>("shownAnimalId"))
    val shownAnimalId: StateFlow<Int?> = _shownAnimalId.asStateFlow()


    val shownAnimal = purchasedImages.combine(shownAnimalId) { images, id ->
        if (id == null) null else images.find { it.id == id }
    }


    fun isAnimalShown(animalId: Int): Boolean {
        return _shownAnimalId.value == animalId
    }


    fun toggleAnimalVisibility(animal: PurchasedImageEntity, context: Context) {
        val sharedPreferences = context.getSharedPreferences("animal_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val newValue = if (_shownAnimalId.value == animal.id) null else animal.id
        _shownAnimalId.value = newValue

        if (newValue == null) {
            // Если скрываем животное, удаляем запись
            editor.remove("shown_animal_id")
        } else {
            // Если показываем животное, сохраняем его ID
            editor.putInt("shown_animal_id", newValue)
        }
        editor.apply()
    }


    fun loadShownAnimalId(context: Context) {
        val sharedPreferences = context.getSharedPreferences("animal_prefs", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("shown_animal_id")) {
            _shownAnimalId.value = sharedPreferences.getInt("shown_animal_id", -1)
        } else {
            _shownAnimalId.value = null
        }
    }

    fun updatePetNameAndVisibility(
        animal: PurchasedImageEntity,
        customName: String,
        context: Context
    ) {
        viewModelScope.launch {

            purchasedImageRepository.updatePetName(animal.id, customName)


            toggleAnimalVisibility(animal.copy(customName = customName), context)
        }
    }


    fun purchaseImage(animal: Animal) {
        viewModelScope.launch {
            val currentCount = clickCount.first()
            val priceToPay = getCurrentPrice(animal)

            if (currentCount >= priceToPay) {
                clickerRepository.saveClickCount(currentCount - priceToPay)

                val animalType = when {
                    animal.price >= 10000 -> "legendary"
                    animal.price >= 5000 -> "fantastic"
                    animal.price >= 1000 -> "exotic"
                    else -> "regular"
                }

                purchasedImageRepository.addPurchasedImage(
                    PurchasedImageEntity(
                        imageName = animal.name,
                        imageResId = animal.imageResId,
                        price = priceToPay, // сохраняем цену покупки
                        animalType = animalType,
                    )
                )
            }
        }
    }

    suspend fun getCurrentPrice(animal: Animal): Int {
    // Считаем, сколько раз уже куплен этот питомец
    val count = purchasedImages.first().count { it.imageName == animal.name }
    val basePrice = animal.price
    val multiplier = 1f + 0.5f * count
    return (basePrice * multiplier).toInt()
}
    }
