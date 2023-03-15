package com.example.alexiguitartuner.feat_metronome.data

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
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.example.alexiguitartuner.MainActivity
import com.example.alexiguitartuner.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MetronomeService : LifecycleService() {

    @Inject
    lateinit var repository: MetronomeRepository

    companion object {
        private const val CHANNEL_ID = "METRONOME SERVICE"
        const val START_SERVICE = "START_METRONOME_SERVICE"
        const val STOP_SERVICE = "STOP_METRONOME_SERVICE"
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
            START_SERVICE -> {
                startForegroundNotification()
                repository.playMetronome()
            }
            STOP_SERVICE -> {
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

        val pendingIntent: PendingIntent = Intent(
            this,
            MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_MUTABLE)
        }

        val stopSelf = Intent(this, MetronomeService::class.java)
        stopSelf.action = STOP_SERVICE

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
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(Icon.createWithResource(this, R.drawable.ic_launcher_background))
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
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }


}