package com.nedaluof.radiox.ui.component

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.nedaluof.radiox.service.RadioStationMediaService

/**
 * Created By NedaluOf - 6/13/2024.
 */
@Composable
fun rememberMediaController(
  lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
): State<MediaController?> {
  val appContext = LocalContext.current.applicationContext
  val controllerManager = remember { MediaControllerManager.getInstance(appContext) }
  DisposableEffect(lifecycle) {
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_START -> controllerManager.initialize()
        Lifecycle.Event.ON_STOP -> controllerManager.release()
        else -> {}
      }
    }
    lifecycle.addObserver(observer)
    onDispose { lifecycle.removeObserver(observer) }
  }
  return controllerManager.controller
}


@Stable
internal class MediaControllerManager private constructor(
  private val appContext: Context
) : RememberObserver {

  //region variables
  private var factory: ListenableFuture<MediaController>? = null
  var controller = mutableStateOf<MediaController?>(null)
    private set
  //endregion

  init {
    initialize()
  }

  //region logic
  @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
  internal fun initialize() {
    if (factory == null || factory?.isDone == true) {
      factory = MediaController.Builder(
        appContext,
        SessionToken(appContext, ComponentName(appContext, RadioStationMediaService::class.java))
      ).buildAsync()
    }
    factory?.addListener(
      { controller.value = factory?.let { if (it.isDone) it.get() else null } },
      MoreExecutors.directExecutor()
    )
  }

  internal fun release() {
    factory?.let {
      MediaController.releaseFuture(it)
      controller.value = null
    }
    factory = null
  }

  override fun onAbandoned() {
    release()
  }

  override fun onForgotten() {
    release()
  }

  override fun onRemembered() {}

  //endregion

  companion object {
    @Volatile
    private var instance: MediaControllerManager? = null

    fun getInstance(context: Context): MediaControllerManager {
      return instance ?: synchronized(this) {
        instance ?: MediaControllerManager(context).also { instance = it }
      }
    }
  }
}

fun MediaController?.playItem(
  mediaItem: MediaItem
) {
  stopItem()
  this?.let {
    setMediaItem(mediaItem)
    prepare()
    play()
  }
}

fun MediaController?.stopItem() {
  this?.let {
    if (it.isPlaying || it.isLoading) {
      it.stop()
    }
  }
}