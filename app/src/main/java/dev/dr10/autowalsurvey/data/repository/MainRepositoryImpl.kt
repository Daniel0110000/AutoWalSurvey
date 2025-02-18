package dev.dr10.autowalsurvey.data.repository

import dev.dr10.autowalsurvey.data.local.datastore.StorageSurveyCounter
import dev.dr10.autowalsurvey.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val storageSurveyCounter: StorageSurveyCounter
): MainRepository {

    override suspend fun addSurvey() {
        storageSurveyCounter.addSurvey()
    }

    override suspend fun clearCounter() {
        storageSurveyCounter.clearCounter()
    }

    override fun getSurveyCounter(): Flow<Int> {
        return storageSurveyCounter.getSurveyCounter()
    }


}