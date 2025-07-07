package com.example.clicker3

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class AppContext : Application() {
    companion object {
        lateinit var instance: AppContext
            private set

        val lifecycleScope get() = instance.applicationScope
    }

    private lateinit var applicationScope: CoroutineScope
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationScope = CoroutineScope(SupervisorJob())
    }
}
