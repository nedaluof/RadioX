package com.nedaluof.radiox.data.datasource.remote.response

import com.nedaluof.radiox.data.datasource.local.db.entities.RadioStationEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RadioStationDTO(
  val name: String? = null,
  @Json(name = "favicon") val thumbnail: String? = null,
  val url: String? = null,
  @Json(name = "url_resolved") val urlResolved: String? = null,
  @Json(name = "stationuuid") val stationUuid: String? = null
)

fun RadioStationDTO?.asEntity() = RadioStationEntity(
  stationUuid = this?.stationUuid,
  stationName = this?.name ?: "",
  stationThumbnail = this?.thumbnail ?: "",
  stationStreamUrl = this?.urlResolved ?: (this?.url ?: "")
)