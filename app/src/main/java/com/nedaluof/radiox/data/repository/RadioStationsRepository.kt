package com.nedaluof.radiox.data.repository

import androidx.paging.PagingSource
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationPagingKeyEntity
import com.nedaluof.radiox.data.datasource.remote.response.RadioStationDTO
import retrofit2.Response

/**
 * Created By NedaluOf - 6/28/2024.
 */
interface RadioStationsRepository {

  /**
   * load radio stations list from remote source
   * @see <a href="https://de1.api.radio-browser.info/">Radio Browser</a>
   * @param page
   * @return Response<List<RadioStationDTO>>
   * */
  suspend fun loadRadioStations(
    countryCode: String,
    page: Int
  ): Response<List<RadioStationDTO>>

  /**
   * load radio stations entities list from cache
   * @return PagingSource<Int, RadioStationEntity>
   * */
  fun loadCachedRadioStationsList(): PagingSource<Int, RadioStationEntity>

  /**
   *  @param list of [RadioStationEntity]'s
   * */
  suspend fun insertRadioStationEntitiesList(list: List<RadioStationEntity>)

  /**
   * clear radio stations table
   * */
  suspend fun clearRadioStationsTable()

  /**
   * @param [pagingKeys] list into paging keys table
   * */
  suspend fun insertRadioStationPagingKeys(pagingKeys: List<RadioStationPagingKeyEntity>)

  /**
   * get paging key by its id
   * @param [id]
   * */
  suspend fun getRadioStationPagingKeyById(id: Long): RadioStationPagingKeyEntity?

  /**
   * get last paging key creation time
   * */
  suspend fun getLastCreationTimeOfPagingKey(): Long?

  /**
   * clear radio station paging keys table
   * */
  suspend fun clearRadioStationPagingKeyTable()

  /**
   * provide database transaction block to client
   * */
  suspend fun <T> transactionBlock(
    block: suspend () -> T
  ): T?
}