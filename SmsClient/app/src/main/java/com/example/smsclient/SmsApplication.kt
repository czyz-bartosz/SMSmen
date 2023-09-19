package com.example.smsclient

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.smsclient.data.AppDataRepository

private const val APP_DATA_NAME = "app_data"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_DATA_NAME
)

class SmsApplication : Application() {
    lateinit var appDataRepository: AppDataRepository

    override fun onCreate() {
        super.onCreate()
        appDataRepository = AppDataRepository(dataStore)
    }
}