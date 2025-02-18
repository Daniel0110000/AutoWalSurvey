package dev.dr10.autowalsurvey.domain.repository

import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun addSurvey()
    suspend fun clearCounter()
    fun getSurveyCounter(): Flow<Int>
}