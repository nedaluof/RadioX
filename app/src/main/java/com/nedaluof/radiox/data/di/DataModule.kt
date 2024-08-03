package com.nedaluof.radiox.data.di

import androidx.room.Room
import com.nedaluof.radiox.data.datasource.local.db.RadioXDatabase
import com.nedaluof.radiox.data.datasource.remote.api.RadioXApiService
import com.nedaluof.radiox.data.datasource.remote.api.RadioXApiService.Companion.BASE_URL
import com.nedaluof.radiox.data.repository.RadioStationsRepository
import com.nedaluof.radiox.data.repository.RadioStationsRepositoryImpl
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By NedaluOf - 6/28/2024.
 */

val localModule = module {
  single<RadioXDatabase> {
    Room.databaseBuilder(
      get(), RadioXDatabase::class.java, "RadioX.dp"
    ).build()
  }
}


val remoteModule = module {
  single<OkHttpClient> {
    OkHttpClient.Builder().readTimeout(180, TimeUnit.SECONDS).connectTimeout(180, TimeUnit.SECONDS)
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }).build()
  }

  single<RadioXApiService> {
    Retrofit.Builder().baseUrl(BASE_URL).client(get())
      .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build())).build()
      .create(RadioXApiService::class.java)
  }
}

val repositoryModule = module {
  single<RadioStationsRepository> { RadioStationsRepositoryImpl(get(), get()) }
}

val dataModule = listOf(localModule, remoteModule, repositoryModule)