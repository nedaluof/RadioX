package com.nedaluof.radiox.data.datasource.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nedaluof.radiox.domain.model.RadioStationModel

/**
 * Created By NedaluOf - 6/28/2024.
 */
@Entity("radio_stations")
data class RadioStationEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  @ColumnInfo("stationuuid") val stationUuid: String? = null,
  @ColumnInfo("station_name") val stationName: String,
  @ColumnInfo("station_thumbnail") val stationThumbnail: String,
  @ColumnInfo("station_stream_url") val stationStreamUrl: String,
)

fun RadioStationEntity?.asModel() = RadioStationModel(
  this?.id ?: 0L,
  this?.stationName ?: "",
  this?.stationThumbnail ?: "",
  this?.stationStreamUrl ?: ""
)