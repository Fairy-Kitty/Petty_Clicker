package com.example.clicker3.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker3.data.repository.ClickerRepository
import com.example.clicker3.data.repository.PurchasedImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive
import javax.inject.Inject

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val repository: ClickerRepository,
    private val purchasedImageRepository: PurchasedImageRepository
) : ViewModel() {
    private val _clickCount = MutableStateFlow(0)
    val clickCount: StateFlow<Int> = _clickCount

    // Для анимации автоклика
    private val _autoClickEvent = MutableStateFlow(false)
    val autoClickEvent: StateFlow<Boolean> = _autoClickEvent

    private var autoClickerStarted = false // Флаг для автокликера

    init {
        viewModelScope.launch {
            repository.clickCount.collect { count ->
                _clickCount.value = count
            }
        }
    }

    // Прибавляет клик с бонусом (по умолчанию 1)
    fun incrementCount(bonus: Int = 1) {
        val currentCount = _clickCount.value
        val newCount = currentCount + bonus
        _clickCount.value = newCount

        viewModelScope.launch {
            repository.saveClickCount(newCount)
        }
    }

    // Автокликер для золотого легендарного питомца
    fun startLegendaryAutoClicker() {
        if (autoClickerStarted) return // Не запускать второй раз
        autoClickerStarted = true
        viewModelScope.launch {
            while (isActive) {
                val pets = purchasedImageRepository.purchasedImages.first()
                val hasGoldenLegendary = pets.any {
                    it.animalType == "legendary"
                }
                if (hasGoldenLegendary) {
                    delay(5_000) // 5 секунд для теста, поставь 510_000 для реального времени
                    val bonus = calculateBonusClicks()
                    incrementCount(bonus)
                    _autoClickEvent.value = true // Сообщаем UI о событии автоклика
                } else {
                    delay(30_000) // Проверяем каждые 30 секунд
                }
            }
        }
    }

    // Сброс события автоклика (вызывай из UI после анимации)
    fun resetAutoClickEvent() {
        _autoClickEvent.value = false
    }

    // Бизнес-логика для бонусных кликов
    suspend fun calculateBonusClicks(): Int {
        var bonus = 1 // Базовый клик
        val pets = purchasedImageRepository.purchasedImages.first()
        val grouped = pets.groupBy { it.imageName }
        for ((_, list) in grouped) {
            val pet = list.first()
            val count = list.size
            if (pet.price < 500) {
                if (count > 1) {
                    bonus += 2 * (count - 1)
                }
            } else {
                bonus += 4 * count
            }
        }
        return bonus
    }
}