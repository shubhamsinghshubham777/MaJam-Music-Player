package com.shubham.majam.exoplayer

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.shubham.majam.exoplayer.callbacks.MusicPlaybackPreparer
import com.shubham.majam.exoplayer.callbacks.MusicPlayerEventsListener
import com.shubham.majam.exoplayer.callbacks.MusicPlayerNotificationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

/* We're implementing this MediaBrowserServiceCompat here because this class contains a lot of tools
* which allows us to implement a file manager-like application e.g. an app like Spotify is almost
* like a file browser where the user can go through albums and playlists just like in a file manager */

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job() //A service is not Asynchronous by Default. It runs on the main thread.
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob) //This ensures cancellation of coroutines when the service dies
    //The + operator above means that it will merge properties of Dispatchers.Main and serviceJob together for our custom defined serviceScope

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var currPlayingSong: MediaMetadataCompat? = null

    override fun onCreate() {
        super.onCreate()
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        } /* This activityIntent is for the notification i.e. when the user clicks on the music notification, he
         should be brought to a specific activity within the app */

        //A mediaSession contains all the important data about the current music session of the user
        //Information like, it informs the Android OS that a media is playing so that it can apply media actions like vol up/down to your media only
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
                this,
                mediaSession.sessionToken,
                MusicPlayerNotificationListener(this)
        ) {

        }

        val musicPlaybackPreparer = MusicPlaybackPreparer(firebaseMusicSource) {
            //This will be used every time the user chooses a new song
            currPlayingSong = it
            preparePlayer(
                    firebaseMusicSource.songs,
                    it,
                    true
            )
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setPlayer(exoPlayer)

        exoPlayer.addListener(MusicPlayerEventsListener(this))
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(
            songs: List<MediaMetadataCompat>,
            itemToPlay: MediaMetadataCompat?,
            playNow: Boolean
    ) {
        val curSongIndex = if(currPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        exoPlayer.prepare(firebaseMusicSource.asMediaSource(dataSourceFactory))
        exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
    }
}