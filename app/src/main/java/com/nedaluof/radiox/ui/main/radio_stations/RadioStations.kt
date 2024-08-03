package com.nedaluof.radiox.ui.main.radio_stations

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.net.toUri
import androidx.media3.session.MediaController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.nedaluof.radiox.R
import com.nedaluof.radiox.domain.model.RadioStationModel
import com.nedaluof.radiox.ui.component.playItem
import com.nedaluof.radiox.ui.component.rememberMediaController
import com.nedaluof.radiox.ui.component.stopItem
import com.nedaluof.radiox.utils.catchOn
import com.nedaluof.radiox.utils.toMediaItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

/**
 * Created By NedaluOf - 6/23/2024.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadioStationsScreen(
  modifier: Modifier = Modifier, viewModel: RadioStationsViewModel = koinViewModel()
) {
  val mediaController by rememberMediaController()
  val radioStations: LazyPagingItems<RadioStationModel> =
    viewModel.radioStations.collectAsLazyPagingItems()
  val pagerState = rememberPagerState(pageCount = { radioStations.itemCount })
  val currentRadioStationModel by remember { viewModel.currentRadioStationModel }
  LaunchedEffect(currentRadioStationModel) {
    currentRadioStationModel?.let { model ->
      mediaController.playItem(model.toMediaItem())
    }
  }

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      catchOn({
        if (radioStations.itemCount > 0) {
          viewModel.currentRadioStationModel.value = radioStations[page]
        }
      })
    }
  }

  Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
    HorizontalPager(
      pageSpacing = 16.dp,
      beyondBoundsPageCount = 2,
      state = pagerState,
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary)
        .padding(innerPadding),
    ) { page ->
      mediaController?.let { safeMediaController ->
        Box(modifier = Modifier.fillMaxSize()) {
          RadioStationCard(
            modifier = Modifier
              .padding(24.dp)
              .align(Alignment.Center),
            model = radioStations[page],
            pagerState = pagerState,
            mediaController = safeMediaController
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadioStationCard(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
  model: RadioStationModel?,
  mediaController: MediaController
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.background),
    shape = RoundedCornerShape(32.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
  ) {
    val currentPage = pagerState.currentPage
    val scope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(currentPage != 0) }
    LaunchedEffect(key1 = isPlaying) {
      if (isPlaying) {
        mediaController.stopItem()
      } else {
        model?.let {
          mediaController.playItem(model.toMediaItem())
        }
      }
    }
    Column(modifier = Modifier) {
      val pageOffset = pagerState.getOffsetFractionForPage(currentPage).absoluteValue
      Image(
        modifier = Modifier
          .padding(32.dp)
          .clip(RoundedCornerShape(24.dp))
          .aspectRatio(1f)
          .background(Color.White)
          .graphicsLayer {
            val scale = lerp(1f, 1.75f, pageOffset)
            scaleX *= scale
            scaleY *= scale
          }, painter = rememberAsyncImagePainter(
          model = model?.thumbnail?.toUri()?.buildUpon()?.scheme("https")?.build() ?: Uri.EMPTY,
          error = painterResource(id = R.drawable.ic_radio)
        ), contentScale = ContentScale.Crop, contentDescription = "Radio Station Image"
      )
      Text(
        model?.name ?: "",
        fontSize = 24.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
      )
      Box(modifier = Modifier
        .height(150.dp * (1 - pageOffset))
        .fillMaxWidth()
        .graphicsLayer {
          alpha = 1 - pageOffset
        }) {
        PlayerController(
          modifier = Modifier.align(Alignment.BottomCenter),
          isPlaying = isPlaying,
          canScrollForward = pagerState.canScrollForward,
          canScrollBackward = pagerState.canScrollBackward,
          onPausePlayToggled = { isPlaying = !isPlaying },
          onForwardClicked = {
            if (pagerState.canScrollForward) {
              scope.launch {
                pagerState.animateScrollToPage(currentPage + 1)
              }
            }
          },
          onBackwardClicked = {
            if (pagerState.canScrollBackward) {
              scope.launch {
                pagerState.animateScrollToPage(currentPage - 1)
              }
            }
          },
        )
      }
    }
  }
}

@Composable
private fun PlayerController(
  modifier: Modifier = Modifier,
  isPlaying: Boolean,
  canScrollForward: Boolean = false,
  canScrollBackward: Boolean = false,
  onPausePlayToggled: () -> Unit,
  onBackwardClicked: () -> Unit,
  onForwardClicked: () -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 50.dp)
  ) {
    Row(modifier = Modifier.align(Alignment.Center)) {
      if (canScrollBackward) {
        IconButton(
          modifier = modifier, onClick = onBackwardClicked
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_forward),
            modifier = Modifier
              .fillMaxSize(fraction = .8f)
              .rotate(180f),
            contentDescription = "Backward",
          )
        }
      }
      IconButton(
        modifier = modifier, onClick = onPausePlayToggled
      ) {
        Icon(
          modifier = Modifier.fillMaxSize(fraction = .8f),
          imageVector = if (!isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
          contentDescription = if (!isPlaying) "Pause" else "Play",
        )
      }
      if (canScrollForward) {
        IconButton(
          modifier = modifier, onClick = onForwardClicked
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_forward),
            modifier = Modifier.fillMaxSize(fraction = .8f),
            contentDescription = "Forward"
          )
        }
      }
    }
  }
}