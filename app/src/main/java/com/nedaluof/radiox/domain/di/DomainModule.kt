package com.nedaluof.radiox.domain.di

import com.nedaluof.radiox.domain.usecase.radiostations.GetRadioStationsUseCase
import com.nedaluof.radiox.domain.usecase.radiostations.GetRadioStationsUseCaseImpl
import org.koin.dsl.module

/**
 * Created By NedaluOf - 6/30/2024.
 */

val useCaseModule = module {
  single<GetRadioStationsUseCase> { GetRadioStationsUseCaseImpl(get(), get()) }
}

val domainModule = listOf(useCaseModule)