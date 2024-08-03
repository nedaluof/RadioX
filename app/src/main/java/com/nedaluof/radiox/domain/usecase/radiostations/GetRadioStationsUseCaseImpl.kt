package com.nedaluof.radiox.domain.usecase.radiostations

import android.content.Context
import android.telephony.TelephonyManager
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nedaluof.radiox.data.datasource.local.db.entities.asModel
import com.nedaluof.radiox.data.repository.RadioStationsRepository
import com.nedaluof.radiox.domain.model.RadioStationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created By NedaluOf - 6/30/2024.
 */
class GetRadioStationsUseCaseImpl(
  private val repository: RadioStationsRepository,
  private val context: Context
) : GetRadioStationsUseCase {

  //region logic
  @OptIn(ExperimentalPagingApi::class)
  override fun loadRadioStationsList(): Flow<PagingData<RadioStationModel>> {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = telephonyManager.networkCountryIso
    return Pager(
      config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
      ),
      pagingSourceFactory = { repository.loadCachedRadioStationsList() },
      remoteMediator = RadioStationsRemoteMediator(countryCode, repository)
    ).flow.map {
      it.map { entity -> entity.asModel() }
    }
  }
  //endregion
}