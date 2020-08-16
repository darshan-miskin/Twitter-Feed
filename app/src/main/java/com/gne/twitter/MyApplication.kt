package com.gne.twitter

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = TwitterConfig.Builder(this)
//                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(BuildConfig.api_key, BuildConfig.api_secret))
//                .debug(true)
                .build()
        Twitter.initialize(config)
        Fresco.initialize(applicationContext,
                ImagePipelineConfig.newBuilder(applicationContext)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build())
    }
}