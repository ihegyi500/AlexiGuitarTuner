package com.example.alexiguitartuner.feat_metronome.data.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkBuilder
import com.example.alexiguitartuner.MainActivity
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepositoryImpl
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MetronomeService : LifecycleService() {

    @Inject
    lateinit var repository: MetronomeRepository

    enum class Actions {
        START, STOP
    }

    companion object {
        private const val CHANNEL_ID = "METRONOME SERVICE"
    }

    private val binder = MetronomeBinder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    inner class MetronomeBinder : Binder() {
        fun getService(): MetronomeService {
            return this@MetronomeService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {
            Actions.START.toString() -> {
                startForegroundNotification()
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        repository.playMetronome()
                    }
                }
            }
            Actions.STOP.toString() -> {
                repository.pauseMetronome()
                stopForeground(STOP_FOREGROUND_DETACH)
                stopService(intent)
            }
        }

        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startForegroundNotification() {

        createNotificationChannel()

        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.bottom_nav)
            .setDestination(R.id.navigation_metronome)
            .createPendingIntent()

        val stopSelf = Intent(this, MetronomeService::class.java)
        stopSelf.action = Actions.STOP.toString()

        val pStopSelf = PendingIntent.getService(
            this,
            0,
            stopSelf,
            PendingIntent.FLAG_MUTABLE)


        val stopAction = Notification.Action.Builder(
            Icon.createWithResource(
                this,
                android.R.drawable.ic_media_pause
            ),
            "Stop",
            pStopSelf
        ).build()

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Metronome")
            .setContentText("Metronome notification")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
            .setColor(resources.getColor(R.color.colorPrimary, null))
            .setContentIntent(pendingIntent)
            .addAction(stopAction)
            .setDeleteIntent(pStopSelf)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }


}