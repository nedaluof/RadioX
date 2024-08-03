package com.nedaluof.radiox.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nedaluof.radiox.data.datasource.local.db.dao.RadioStationPagingKeysDao
import com.nedaluof.radiox.data.datasource.local.db.dao.RadioStationsDao
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity
import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationPagingKeyEntity

/**
 * Created By NedaluOf - 6/28/2024.
 */
@Database(
  entities = [RadioStationEntity::class, RadioStationPagingKeyEntity::class],
  version = 1,
  exportSchema = false
)
abstract class RadioXDatabase : RoomDatabase() {
  abstract val radioStationsDao: RadioStationsDao
  abstract val radioStationsPagingKeysDao: RadioStationPagingKeysDao
}