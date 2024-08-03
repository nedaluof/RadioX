package com.nedaluof.radiox.ui.main.radio_stations

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nedaluof.radiox.domain.model.RadioStationModel
import com.nedaluof.radiox.domain.usecase.radiostations.GetRadioStationsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created By NedaluOf - 7/2/2024.
 */
class RadioStationsViewModel(
  useCase: GetRadioStationsUseCase
) : ViewModel() {

  //region variables
  val currentRadioStationModel = mutableStateOf<RadioStationModel?>(null)
  val isPlaying = mutableStateOf(true)
  val radioStations: Flow<PagingData<RadioStationModel>> =
    useCase.loadRadioStationsList().cachedIn(viewModelScope)
  //endregion
}