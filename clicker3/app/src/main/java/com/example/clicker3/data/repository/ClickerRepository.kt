package com.example.clicker3.data.repository


import com.example.clicker3.data.dao.ClickerDao
import com.example.clicker3.data.entity.ClickerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClickerRepository @Inject constructor(private val clickerDao: ClickerDao) {
    val clickCount: Flow<Int> = clickerDao.getClickerData().map {
        it?.clickCount ?: 0
    }

    suspend fun saveClickCount(count: Int) {
        clickerDao.saveClickerData(ClickerEntity(clickCount = count))
    }
}
