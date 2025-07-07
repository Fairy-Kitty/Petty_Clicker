package com.example.clicker3.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.clicker3.AppContext

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object ThemeManager {
    private const val THEME_KEY = "dark_theme"


    private val dataStore get() = AppContext.instance.dataStore

    val isDarkTheme: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[booleanPreferencesKey(THEME_KEY)] ?: false
        }
        .stateIn(
            scope = AppContext.lifecycleScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = runBlocking {
                dataStore.data.first()[booleanPreferencesKey(THEME_KEY)] ?: false
            }
        )

    fun toggleTheme() {
        AppContext.lifecycleScope.launch {
            dataStore.edit { settings ->
                val current = settings[booleanPreferencesKey(THEME_KEY)] ?: false
                settings[booleanPreferencesKey(THEME_KEY)] = !current
            }
        }
    }
}

