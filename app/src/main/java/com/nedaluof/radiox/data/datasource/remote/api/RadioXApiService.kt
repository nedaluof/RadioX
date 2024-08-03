package com.nedaluof.radiox.data.datasource.remote.api

import com.nedaluof.radiox.data.datasource.remote.response.RadioStationDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created By NedaluOf - 6/28/2024.
 */
interface RadioXApiService {

  @GET("json/stations/bycountrycodeexact/{country_code}")
  suspend fun loadRadioStations(
    @Path("country_code") countryCode: String,
    @Query("page") page: Int = 1,
    @Query("limit") limit: Int = 20
  ): Response<List<RadioStationDTO>>

  companion object {
    const val BASE_URL = "https://de1.api.radio-browser.info/"
  }
}