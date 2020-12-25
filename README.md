# MaJam Music Player
------------
A beautiful looking music player for Android illustrating the MVVM coding architecture and the best practices for developing native Android applications using Kotlin.

### Features 

------------

1. A new song-playlist automatically updated everyday! :fire:
2. No need to download songs (All songs will be available on an online server)
3. Best sound quality (Lossless FLAC files, no compromises here)
4. Attractive UI :heart:
5. Low app-size

------------

**Disclaimer**: This app will use a lot of data since it will be streaming high-quality lossless FLAC files. Therefore the users are requested NOT to use it on limited mobile/WiFi data plans.

------------

### Screenshots

------------

| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img width="1604" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://github.com/shubhamsinghshubham777/MaJam/blob/master/Screenshots/ForegroundService.png"> |  <img width="1604" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://github.com/shubhamsinghshubham777/MaJam/blob/master/Screenshots/HomeFragment.png">|<img width="1604" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://github.com/shubhamsinghshubham777/MaJam/blob/master/Screenshots/SongFragment.png">|

------------

### Tools Used

------------
1. Kotlin
2. [Google Firebase](https://firebase.google.com/ "Google Firebase") for backend
3. [Airbnb Lottie](https://airbnb.io/lottie/#/ "Airbnb Lottie") for animations
4. [Glide](https://github.com/bumptech/glide "Glide Image Loading Library") Image Loading Library
5. [ExoPlayer](https://github.com/google/ExoPlayer "Google ExoPlayer")
6. [Dagger Hilt](https://dagger.dev/hilt/ "Dagger Hilt")
------------

### Dependencies

------------
    // Architectural Components
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"

    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-runtime:2.2.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'

    // Coroutine Lifecycle Scopes
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

    // Navigation Component
    implementation "androidx.navigation:navigation-fragment-ktx:2.3.0"
    implementation "androidx.navigation:navigation-ui-ktx:2.3.0"

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    kapt 'com.github.bumptech.glide:compiler:4.11.0'

    // Activity KTX for viewModels()
    implementation "androidx.activity:activity-ktx:1.1.0"

    //Dagger - Hilt
    implementation "com.google.dagger:hilt-android:2.28-alpha"
    kapt "com.google.dagger:hilt-android-compiler:2.28-alpha"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02"
    kapt "androidx.hilt:hilt-compiler:1.0.0-alpha02"

    // Timber
    implementation 'com.jakewharton.timber:timber:4.7.1'

    // Firebase Firestore
    implementation 'com.google.firebase:firebase-firestore:22.0.0'


    // Firebase Storage KTX
    implementation 'com.google.firebase:firebase-storage-ktx:19.2.0'

    // Firebase Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1'

    // ExoPlayer
    api "com.google.android.exoplayer:exoplayer-core:2.11.8"
    api "com.google.android.exoplayer:exoplayer-ui:2.11.8"
    api "com.google.android.exoplayer:extension-mediasession:2.11.8"

    //Lottie
    implementation 'com.airbnb.android:lottie:3.5.0'
	
	//Material Design
    implementation "com.google.android.material:material:1.3.0-alpha02"
