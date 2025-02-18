package dev.dr10.autowalsurvey.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.dr10.autowalsurvey.data.local.datastore.StorageSurveyCounter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providerStorageSurveyCounter(
        @ApplicationContext context: Context
    ): StorageSurveyCounter = StorageSurveyCounter(context)

}