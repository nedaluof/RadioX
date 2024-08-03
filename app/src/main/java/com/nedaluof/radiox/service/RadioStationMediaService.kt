package com.nedaluof.radiox.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.nedaluof.radiox.utils.catchOn

/**
 * Created By NedaluOf - 7/3/2024.
 */
class RadioStationMediaService : MediaSessionService() {

  //region variables
  private var mediaSession: MediaSession? = null
  //endregion

  //region logic
  @OptIn(UnstableApi::class)
  override fun onCreate() {
    super.onCreate()
    catchOn({
      mediaSession = MediaSession.Builder(
        this, ExoPlayer.Builder(this).build()
      ).setShowPlayButtonIfPlaybackIsSuppressed(false).build()
    })
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    val player = mediaSession?.player
    player?.let {
      if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
        stopSelf()
      }
    }
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

  override fun onDestroy() {
    mediaSession?.run {
      player.release()
      release()
      mediaSession = null
    }
    super.onDestroy()
  }
  //endregion
}