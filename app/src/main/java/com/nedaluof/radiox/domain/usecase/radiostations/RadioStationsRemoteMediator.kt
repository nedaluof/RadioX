package com.nedaluof.radiox.domain.usecase.radiostations

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationPagingKeyEntity
import com.nedaluof.radiox.data.datasource.remote.response.asEntity
import com.nedaluof.radiox.data.repository.RadioStationsRepository
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created By NedaluOf - 6/30/2024.
 */
@OptIn(ExperimentalPagingApi::class)
class RadioStationsRemoteMediator(
  private val countryCode: String, private val repository: RadioStationsRepository
) : RemoteMediator<Int, RadioStationEntity>() {

  override suspend fun initialize(): InitializeAction {
    val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    return if (System.currentTimeMillis() - (repository.getLastCreationTimeOfPagingKey()
        ?: 0) < cacheTimeout
    ) {
      InitializeAction.SKIP_INITIAL_REFRESH
    } else {
      InitializeAction.LAUNCH_INITIAL_REFRESH
    }
  }

  private suspend fun getPagingKeyClosestToCurrentPosition(state: PagingState<Int, RadioStationEntity>): RadioStationPagingKeyEntity? {
    return state.anchorPosition?.let { position ->
      state.closestItemToPosition(position)?.let { entity ->
        repository.getRadioStationPagingKeyById(entity.id)
      }
    }
  }

  private suspend fun getPagingKeyForFirstItem(state: PagingState<Int, RadioStationEntity>): RadioStationPagingKeyEntity? {
    return state.pages.firstOrNull {
      it.data.isNotEmpty()
    }?.data?.firstOrNull()?.let { entity ->
      repository.getRadioStationPagingKeyById(entity.id)
    }
  }

  private suspend fun getPagingKeyForLastItem(state: PagingState<Int, RadioStationEntity>): RadioStationPagingKeyEntity? {
    return state.pages.lastOrNull {
      it.data.isNotEmpty()
    }?.data?.lastOrNull()?.let { entity ->
      repository.getRadioStationPagingKeyById(entity.id)
    }
  }

  override suspend fun load(
    loadType: LoadType, state: PagingState<Int, RadioStationEntity>
  ): MediatorResult {
    val currentPage: Int = when (loadType) {
      LoadType.REFRESH -> {
        val pagingKey = getPagingKeyClosestToCurrentPosition(state)
        pagingKey?.nextKey?.minus(PAGE) ?: PAGE
      }

      LoadType.PREPEND -> {
        val pagingKey = getPagingKeyForFirstItem(state)
        pagingKey?.prevKey
          ?: return MediatorResult.Success(endOfPaginationReached = pagingKey != null)
      }

      LoadType.APPEND -> {
        val pagingKey = getPagingKeyForLastItem(state)
        pagingKey?.nextKey ?: PAGE
      }
    }

    try {
      val apiResponse = repository.loadRadioStations(countryCode, currentPage)
      val radioStations = apiResponse.body()?.map { dto -> dto.asEntity() }
      val dataList = radioStations ?: emptyList()
      val endOfPaginationReached = dataList.isEmpty()

      /**
       * to avoid rate limit error from the api owners
       * which is 100 request per minute
       * */
      delay(1000)

      repository.transactionBlock {
        if (loadType == LoadType.REFRESH) {
          //clear tables if LoadType is REFRESH
          repository.clearRadioStationPagingKeyTable()
          repository.clearRadioStationsTable()
        }

        //calculate new keys
        val prevKey = if (currentPage > PAGE) currentPage - PAGE else null
        val nextKey = if (endOfPaginationReached) null else currentPage + PAGE

        val pagingKeys = dataList.map { station ->
          RadioStationPagingKeyEntity(
            station.stationUuid ?: "",
            prevKey,
            currentPage,
            nextKey,
          )
        }
        repository.insertRadioStationPagingKeys(pagingKeys)
        repository.insertRadioStationEntitiesList(dataList)
      }
      return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (exception: Exception) {
      Timber.e(exception.message)
      return MediatorResult.Error(exception)
    }
  }

  companion object {
    private const val PAGE = 1
  }
}