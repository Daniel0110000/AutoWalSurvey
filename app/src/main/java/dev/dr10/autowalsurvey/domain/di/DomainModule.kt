package dev.dr10.autowalsurvey.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.dr10.autowalsurvey.data.repository.MainRepositoryImpl
import dev.dr10.autowalsurvey.domain.repository.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindSurveyRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository
}