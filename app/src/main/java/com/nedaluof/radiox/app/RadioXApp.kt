package com.nedaluof.radiox.app

import android.app.Application
import com.nedaluof.radiox.data.di.dataModule
import com.nedaluof.radiox.ui.di.viewModelsModule
import com.nedaluof.radiox.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created By NedaluOf - 6/23/2024.
 */
class RadioXApp : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@RadioXApp)
      modules(dataModule)
      modules(domainModule)
      modules(viewModelsModule)
    }
    Timber.plant(Timber.DebugTree())
  }
}