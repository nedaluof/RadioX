package com.nedaluof.radiox.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationPagingKeyEntity

/**
 * Created By NedaluOf - 6/30/2024.
 */
@Dao
interface RadioStationPagingKeysDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRadioStationPagingKeys(pagingKeys: List<RadioStationPagingKeyEntity>)

  @Query("Select * From radio_station_paging_key Where radio_station_id = :radioStationId")
  suspend fun getRadioStationPagingKeyId(radioStationId: Long): RadioStationPagingKeyEntity?

  @Query("Delete From radio_station_paging_key")
  suspend fun clearRadioStationPagingKeyTable()

  @Query("Select created_at From radio_station_paging_key Order By created_at DESC LIMIT 1")
  suspend fun getLastCreationTime(): Long?
}