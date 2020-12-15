package com.shubham.majam.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.shubham.majam.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) //Only describes lifecycle for all these modules (i.e. only till Application lives)
object AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(
            @ApplicationContext context: Context //this is included because an ApplicationContext is generally anyways needed to create a glide object
    ) = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}