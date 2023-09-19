package com.example.smsclient

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smsclient.ui.SmsHomeScreenViewModel
import com.example.smsclient.ui.theme.SMSClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = (application as SmsApplication)
                    SmsService.startService(applicationContext, app.appDataRepository)
                    SmsClientApp(applicationContext, this)
                }
            }
        }
    }
}

@Composable
fun SmsClientApp(
    context: Context,
    activity: ComponentActivity
) {
    SMSHomeScreen(context = context, activity = activity)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SMSHomeScreen(
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier
) {
    val viewModel: SmsHomeScreenViewModel = viewModel(factory = SmsHomeScreenViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
    ) {
        TextField(
            value = uiState.serverUrl,
            onValueChange = viewModel::setServerUrl,
            label = { Text("server url") }
        )

        Button(onClick = {
            viewModel.saveServerUrl()
        }) {
            Text("save")
        }

        Button(onClick = {
            viewModel.requestPermission(context, activity)
        }) {
            Text("give send sms permission")
        }
    }
}