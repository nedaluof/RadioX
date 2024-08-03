package com.nedaluof.radiox.ui.di

import com.nedaluof.radiox.ui.main.radio_stations.RadioStationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created By NedaluOf - 7/2/2024.
 */
val viewModelsModule = module {
  viewModel<RadioStationsViewModel> { RadioStationsViewModel(get()) }
}