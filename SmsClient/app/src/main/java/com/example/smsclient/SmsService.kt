package com.example.smsclient

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.smsclient.data.AppDataRepository
import com.example.smsclient.data.SmsModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SmsService: Service() {
    private lateinit var smsManager: SmsManager
    private lateinit var client: HttpClient
    private lateinit var scope: CoroutineScope
    private lateinit var notificationManager: NotificationManager
    private var isStarted = false
    private var serverUrl = ""

    override fun onCreate() {
        super.onCreate()
        // initialize dependencies here (e.g. perform dependency injection)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        smsManager = getSystemService(SmsManager::class.java)
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        scope = CoroutineScope(Dispatchers.Main)
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
        scope.cancel()
        client.close()
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isStarted) {
            makeForeground()
            isStarted = true
            scope.launch {
                appDataRepository.serverUrl.collectLatest {
                    serverUrl = it
                }
            }
            scope.launch {
                while(true) {
                    delay(1000)
                    try {
                        val response: HttpResponse = client.get(serverUrl) {
                            url {
                                appendPathSegments("api", "sms")
                            }
                        }
                        val status = response.status.value
                        if (status == 200) {
                            val body: SmsModel = response.body()
                            sendMessage(body.phoneNumber, body.message)
                        }
                    }catch(e: Throwable) {
                        Log.d(TAG, "${e.message}")
                    }
                }
            }
        }

        return START_STICKY
    }

    private fun sendMessage(phoneNumber: String, message: String) {
            smsManager.sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )
    }

    private fun makeForeground() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // before calling startForeground, we must create a notification and a corresponding
        // notification channel

        createServiceNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground Service demonstration")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun createServiceNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "SMS_SERVICE"
        private const val ONGOING_NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "1001"
        private lateinit var appDataRepository: AppDataRepository

        fun startService(context: Context, appDataRepository: AppDataRepository) {
            this.appDataRepository = appDataRepository
            val intent = Intent(context, SmsService::class.java)
            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, SmsService::class.java)
            context.stopService(intent)
        }
    }
}