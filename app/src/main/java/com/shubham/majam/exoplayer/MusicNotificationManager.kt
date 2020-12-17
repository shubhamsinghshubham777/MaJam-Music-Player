package com.shubham.majam.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.shubham.majam.R
import com.shubham.majam.other.Constants.NOTIFICATION_CHANNEL_ID
import com.shubham.majam.other.Constants.NOTIFICATION_ID

class MusicNotificationManager(
        private val context: Context,
        sessionToken: MediaSessionCompat.Token,
        notificationListener: PlayerNotificationManager.NotificationListener, /* This is a listener that
        contains functions that can be used after the notification is created e.g. when user swipes
        away the notification, we need to stop the foreground service (media) */
        private val newSongCallback: () -> Unit /* Here we can detect when a new song starts playing.
        It can be used to set current duration of the song*/
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                NOTIFICATION_CHANNEL_ID,
                R.string.notification_channel_name,
                R.string.notification_channel_description,
                NOTIFICATION_ID,
                DescriptionAdapter(mediaController),
                notificationListener
        ).apply {
            setSmallIcon(R.drawable.ic_music)
            setMediaSessionToken(sessionToken) /*This will give our notificationManager access to
            current media in our media service*/
        }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    } //This function is called in MusicService class

    private inner class DescriptionAdapter(
            private val mediaController: MediaControllerCompat
    ): PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return mediaController.metadata.description.subtitle
        }

        override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
            Glide.with(context).asBitmap()
                    .load(mediaController.metadata.description.iconUri)
                    .into(object: CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            callback.onBitmap(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) = Unit
                    })
            return null
        }

    }
}