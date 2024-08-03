package com.nedaluof.radiox.utils

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.nedaluof.radiox.domain.model.RadioStationModel
import timber.log.Timber

/**
 * Created By NedaluOf - 7/3/2024.
 */

fun catchOn(
  tryBlock: () -> Unit, exceptionBlock: (Exception) -> Unit = {}
) {
  try {
    tryBlock()
  } catch (e: Exception) {
    e.message.loge()
    exceptionBlock(e)
  }
}

fun String?.loge() = Timber.e("NedaluOf -> $this")

fun RadioStationModel.toMediaItem(): MediaItem = MediaItem.Builder()
  .setMediaId("${this.id}")
  .setUri(Uri.parse(this.streamUrl))
  .setMediaMetadata(
    MediaMetadata.Builder().setTitle(this.name).build()
  ).build()