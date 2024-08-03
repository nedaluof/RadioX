package com.nedaluof.radiox.data.datasource.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By NedaluOf - 6/30/2024.
 */
@Entity(tableName = "radio_station_paging_key")
data class RadioStationPagingKeyEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo(name = "radio_station_id")
  val radioStationId: String,
  @ColumnInfo(name = "previous_key")
  var prevKey: Int? = null,
  @ColumnInfo(name = "current_page")
  var currentPage: Int = 0,
  @ColumnInfo(name = "next_key")
  var nextKey: Int? = null,
  @ColumnInfo(name = "created_at")
  val createdAt: Long = System.currentTimeMillis(),
)