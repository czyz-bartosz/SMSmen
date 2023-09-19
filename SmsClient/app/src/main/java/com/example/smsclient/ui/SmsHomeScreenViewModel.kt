package com.example.smsclient.ui

import android.Manifest
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smsclient.data.AppDataRepository
import com.example.smsclient.SmsApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmsHomeScreenViewModel(
    private val appDataRepository: AppDataRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SmsApplication)
                SmsHomeScreenViewModel(application.appDataRepository)
            }
        }
    }

    private var _uiState = MutableStateFlow(SmsHomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            appDataRepository.serverUrl.collectLatest { serverUrl ->
                _uiState.update {
                    SmsHomeScreenUiState(serverUrl)
                }
            }
        }
    }

    fun setServerUrl(serverUrl: String) {
        _uiState.update {
            it.copy(
                serverUrl = serverUrl
            )
        }
    }

    fun saveServerUrl() {
        viewModelScope.launch {
            appDataRepository.saveServerUrl(_uiState.value.serverUrl)
        }
    }

    fun requestPermission(applicationContext: Context, activity: ComponentActivity) {
//        if(ContextCompat.checkSelfPermission(applicationContext,
//                Manifest.permission.SEND_SMS
//            ) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.SEND_SMS), 100)
//        }
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.SEND_SMS), 0)
    }
}