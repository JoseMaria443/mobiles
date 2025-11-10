package com.example.examen.viewModel.themeViewModel

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeViewModel(private val context: Context): ViewModel() {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("theme")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val getTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[DARK_THEME]?:false }

    private suspend fun saveThemeInternally(theme: Boolean){
        context.dataStore.edit { preferences -> preferences[DARK_THEME] = theme }
    }

    fun saveTheme(theme: Boolean){
        viewModelScope.launch {
            saveThemeInternally(theme)
        }
    }
}