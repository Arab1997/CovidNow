package io.github.omisie11.coronatracker.ui.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.omisie11.coronatracker.data.local.model.GlobalSummary
import io.github.omisie11.coronatracker.data.repository.GlobalSummaryRepository
import io.github.omisie11.coronatracker.vo.FetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GlobalViewModel(private val repository: GlobalSummaryRepository) : ViewModel() {

    private val globalSummary = MutableLiveData<GlobalSummary>()
    private val isDataFetching: LiveData<Boolean> = repository.getFetchingStatus()
    private val _snackBar: MutableLiveData<FetchResult> = repository.getFetchResult()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGlobalSummaryFlow().collect { globalSummary.postValue(it) }
        }
    }

    fun getGlobalSummary(): LiveData<GlobalSummary> = globalSummary

    fun getDataFetchingStatus(): LiveData<Boolean> = isDataFetching

    fun refreshGlobalSummary(forceRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) { repository.refreshData(forceRefresh) }
    }

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<FetchResult>
        get() = _snackBar

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = FetchResult.OK
    }
}
