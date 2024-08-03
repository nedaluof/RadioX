package com.nedaluof.radiox.domain.usecase.radiostations

import androidx.paging.PagingData
import com.nedaluof.radiox.domain.model.RadioStationModel
import kotlinx.coroutines.flow.Flow

/**
 * Created By NedaluOf - 6/30/2024.
 */
interface GetRadioStationsUseCase {
  fun loadRadioStationsList(): Flow<PagingData<RadioStationModel>>
}