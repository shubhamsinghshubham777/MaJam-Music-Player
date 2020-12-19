package com.shubham.majam.exoplayer.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.shubham.majam.exoplayer.FirebaseMusicSource
import kotlinx.coroutines.InternalCoroutinesApi

/* This class provides us with the functions that will be called on player events such as:
when the ExoPlayer (Music Player) is prepared */
class MusicPlaybackPreparer(
        private val firebaseMusicSource: FirebaseMusicSource,
        private val playerPrepared: (MediaMetadataCompat?) -> Unit //Gives out metadata of current track
) : MediaSessionConnector.PlaybackPreparer {
    override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean = false

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    @InternalCoroutinesApi
    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        firebaseMusicSource.whenReady {
            val itemToPlay = firebaseMusicSource.songs.find {
                mediaId == it.description.mediaId
            }
            playerPrepared(itemToPlay)
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit
}

override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
}
}