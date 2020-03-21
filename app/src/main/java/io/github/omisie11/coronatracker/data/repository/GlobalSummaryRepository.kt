package io.github.omisie11.coronatracker.data.repository

import android.content.SharedPreferences
import io.github.omisie11.coronatracker.data.local.dao.GlobalSummaryDao
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.mappers.DataMappers
import io.github.omisie11.coronatracker.data.remote.ApiService
import io.github.omisie11.coronatracker.data.remote.model.GlobalSummaryRemote
import io.github.omisie11.coronatracker.util.PREFS_LAST_REFRESH_GLOBAL_SUMMARY
import retrofit2.Response

class GlobalSummaryRepository(
    private val apiService: ApiService,
    private val globalSummaryDao: GlobalSummaryDao,
    private val mappers: DataMappers,
    sharedPrefs: SharedPreferences
) : BaseRepository<GlobalSummaryRemote, GlobalSummary>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_GLOBAL_SUMMARY

    fun getGlobalSummaryFlow() = globalSummaryDao.getGlobalSummaryFlow()

    override suspend fun makeApiCall(): Response<GlobalSummaryRemote> =
        apiService.getGlobalSummary()

    override suspend fun saveToDb(data: GlobalSummary) {
        globalSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: GlobalSummaryRemote): GlobalSummary =
        mappers.mapToLocalSummary(data)
}
