package com.example.common.core.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes


fun playRawSoundFile(context: Context, @RawRes fileResId: Int) {
    MediaPlayer.create(context, fileResId).apply {
        start()
    }
}
