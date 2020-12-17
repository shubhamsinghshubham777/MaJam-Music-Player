package com.shubham.majam.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.shubham.majam.data.remote.MusicDatabase
import com.shubham.majam.exoplayer.State.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

//This class will get the list of songs that we got from FireStore DB
class FirebaseMusicSource @Inject constructor(
        private val musicDatabase: MusicDatabase
) {

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    var songs = emptyList<MediaMetadataCompat>()

    @InternalCoroutinesApi
    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = STATE_INITIALIZING
        val allSongs = musicDatabase.getAllSongs()
        songs = allSongs.map {
            MediaMetadataCompat.Builder()
                    .putString(METADATA_KEY_ARTIST, it.subtitle)
                    .putString(METADATA_KEY_MEDIA_ID, it.mediaID)
                    .putString(METADATA_KEY_TITLE, it.title)
                    .putString(METADATA_KEY_DISPLAY_TITLE, it.title)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, it.imageUrl)
                    .putString(METADATA_KEY_MEDIA_URI, it.songUrl)
                    .putString(METADATA_KEY_ALBUM_ART_URI, it.imageUrl)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, it.subtitle)
                    .putString(METADATA_KEY_DISPLAY_DESCRIPTION, it.subtitle)
                    .build()
        }
        state = STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(it.getString(METADATA_KEY_MEDIA_URI).toUri())
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map {
        val desc = MediaDescriptionCompat.Builder()
                .setMediaUri(it.getString(METADATA_KEY_MEDIA_URI).toUri())
                .setTitle(it.description.title)
                .setSubtitle(it.description.subtitle)
                .setMediaId(it.description.mediaId)
                .setIconUri(it.description.iconUri)
                .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }

    @InternalCoroutinesApi
    private var state: State = STATE_CREATED
        set(value) {
            if(value == STATE_INITIALIZED || value == STATE_ERROR) {
                kotlinx.coroutines.internal.synchronized (onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    @InternalCoroutinesApi
    fun whenReady(action: (Boolean)-> Unit): Boolean {
        if(state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners += action
            return false
        } else {
            action(state == STATE_INITIALIZED)
            return true
        }
    }
}

//These states depict the current state of our DataSource (since we need an immediate result whether the data is ready or not in our app)
 enum class State {
    STATE_CREATED,
    STATE_INITIALIZING, //before we start downloading songs
    STATE_INITIALIZED, //after we have downloaded the songs
    STATE_ERROR
}