package com.nedaluof.radiox.data.repository

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.nedaluof.radiox.data.datasource.local.db.RadioXDatabase
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationPagingKeyEntity
import com.nedaluof.radiox.data.datasource.remote.api.RadioXApiService
import com.nedaluof.radiox.data.datasource.remote.response.RadioStationDTO
import retrofit2.Response

/**
 * Created By NedaluOf - 6/28/2024.
 */
class RadioStationsRepositoryImpl(
  private val apiService: RadioXApiService,
  private val database: RadioXDatabase
) : RadioStationsRepository {

  //region variables
  private val radioStationsDao by lazy { database.radioStationsDao }
  private val radioStationPagingKeysDao by lazy { database.radioStationsPagingKeysDao }
  //endregion

  //region logic
  override suspend fun loadRadioStations(
    countryCode: String,
    page: Int
  ): Response<List<RadioStationDTO>> = apiService.loadRadioStations(countryCode, page)

  override fun loadCachedRadioStationsList(): PagingSource<Int, RadioStationEntity> =
    radioStationsDao.loadRadioStations()

  override suspend fun insertRadioStationEntitiesList(list: List<RadioStationEntity>) =
    radioStationsDao.insertRadioStations(list)

  override suspend fun insertRadioStationPagingKeys(pagingKeys: List<RadioStationPagingKeyEntity>) =
    radioStationPagingKeysDao.insertRadioStationPagingKeys(pagingKeys)

  override suspend fun clearRadioStationsTable() = radioStationsDao.clearRadioStationsTable()

  override suspend fun getRadioStationPagingKeyById(id: Long): RadioStationPagingKeyEntity? =
    radioStationPagingKeysDao.getRadioStationPagingKeyId(id)

  override suspend fun getLastCreationTimeOfPagingKey(): Long? =
    radioStationPagingKeysDao.getLastCreationTime()

  override suspend fun clearRadioStationPagingKeyTable() =
    radioStationPagingKeysDao.clearRadioStationPagingKeyTable()

  override suspend fun <T> transactionBlock(block: suspend () -> T): T? =
    database.withTransaction(block)
  //endregion
}