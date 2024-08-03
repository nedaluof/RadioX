package com.nedaluof.radiox.data.datasource.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity

/**
 * Created By NedaluOf - 6/28/2024.
 */
@Dao
interface RadioStationsDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRadioStations(list: List<RadioStationEntity>)

  @Query("select * from radio_stations order by id")
  fun loadRadioStations(): PagingSource<Int, RadioStationEntity>

  @Query("DELETE FROM radio_stations")
  suspend fun clearRadioStationsTable()
}