package com.shubham.majam.di

import android.content.Context
import android.media.AudioAttributes
import com.bumptech.glide.util.Util
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class) //Restricts lifecycle of modules to Service's lifecycle
object ServiceModule {

    @ServiceScoped
    /* can't use singleton here since it's a service and it is not to be used in the
       whole application context but alternatively, @ServiceScoped also does a similar job for services */
    /* if we use ServiceComponent::class in @InstallIn, then we need to use @ServiceScoped instead of @Singleton */
    @Provides
    fun provideAudioAttributes(
            //no object is included since no parameters are generally
    ) = com.google.android.exoplayer2.audio.AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @Provides
    @ServiceScoped
    fun provideExoPlayer(
            @ApplicationContext context: Context,
            audioAttributes: com.google.android.exoplayer2.audio.AudioAttributes
    ) = SimpleExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true) /* This will pause audio on instances where the audio can become
         too noisy for the user to handle e.g. when the user plugs in headphones. Therefore, the user will
         have to press the play button after inserting his headphones */
    }

    @ServiceScoped
    @Provides
    /* Data in your application has to come from somewhere.. right?
    So this function gets you your data source e.g. it can be firebase, ROOM or any other source */
    fun provideDataSourceFactory(
            @ApplicationContext context: Context
    ) = DefaultDataSourceFactory(context, com.google.android.exoplayer2.util.Util.getUserAgent(context, "MaJam App"))
}

