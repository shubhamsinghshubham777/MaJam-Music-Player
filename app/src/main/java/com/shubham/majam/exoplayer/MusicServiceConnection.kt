package com.shubham.majam.exoplayer

import android.content.ComponentName
import android.content.Context
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubham.majam.other.Constants.NETWORK_ERROR
import com.shubham.majam.other.Event
import com.shubham.majam.other.Resource

//This class sits between Activity/Fragment and Service and helps them connect and communicate
class MusicServiceConnection(context: Context) {

    /* All these variables below are LiveData variables which will be used by the activities/fragments
    * to observe their current values. We are making two versions of each variable (i.e. 1 mutable and
    * 1 immutable) since we want to restrict unwanted access to our actual values by giving out only
    * the immutable variables to the activities/fragments. */

    private val _isConnected = MutableLiveData<com.shubham.majam.other.Event<Resource<Boolean>>>()
    val isConnected: LiveData<com.shubham.majam.other.Event<Resource<Boolean>>> = _isConnected

    private val _networkError = MutableLiveData<com.shubham.majam.other.Event<Resource<Boolean>>>()
    val networkError: LiveData<com.shubham.majam.other.Event<Resource<Boolean>>> = _networkError

    private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
    val playbackState: LiveData<PlaybackStateCompat?> = _playbackState

    private val _curPlayingSong = MutableLiveData<MediaMetadataCompat?>()
    val curPlayingSong: LiveData<MediaMetadataCompat?> = _curPlayingSong

    lateinit var mediaController: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
            context,
            ComponentName(
                    context,
                    MusicService::class.java
            ),
            mediaBrowserConnectionCallback,
            null
    ).apply { connect() }

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls /* using get() here because the transportControls
         are not instantiated yet ( observe that the var mediaController is a lateinit var (line 26) )
         therefore, we only want to access it when a value is actually provided otherwise the code
         will crash! */

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unSubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(
            private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.postValue(Event(Resource.success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error(
                    "The connection was suspended!", false
            )))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(Event(Resource.error(
                    "Couldn't connect to media browser", false
            )))
        }

    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        /* This function is called whenever the state of our music player changes and it updates
        * the current value(or state) to _playbackState*/
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.postValue(state)
        }

        /* This function is used whenever our song metadata changes (basically whenever our song changes) */
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _curPlayingSong.postValue(metadata)
        }

        /* This function will be used to notify events such as networkError */
        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> _networkError.postValue(
                        Event(
                                Resource.error(
                                        "Couldn't connect to the server. Please check your internet connection.",
                                        null
                                )
                        )
                )

            }

        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}