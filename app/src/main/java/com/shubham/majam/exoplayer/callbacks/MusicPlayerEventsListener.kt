package com.shubham.majam.exoplayer.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.shubham.majam.exoplayer.MusicService

class MusicPlayerEventsListener(
        private val musicService: MusicService /* We need this access to musicService because we
        need to stop the foreground service from within this listener */
) : Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error occured", Toast.LENGTH_LONG).show()
    }
}