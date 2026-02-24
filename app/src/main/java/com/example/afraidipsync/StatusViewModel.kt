package com.example.afraidipsync

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StatusViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("afraid_ip_sync_prefs", Context.MODE_PRIVATE)
    
    private val _status = MutableStateFlow<StatusResponse>(emptyMap())
    val status: StateFlow<StatusResponse> = _status

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var apiService: ApiService? = null
    
    private val _savedUrl = MutableStateFlow(sharedPrefs.getString("base_url", "") ?: "")
    val savedUrl: StateFlow<String> = _savedUrl

    init {
        val initialUrl = _savedUrl.value
        if (initialUrl.isNotEmpty()) {
            setupApiService(initialUrl)
            refreshStatus()
        }
    }

    fun updateBaseUrl(url: String) {
        val baseUrl = when {
            url.isBlank() -> ""
            url.startsWith("http") -> if (url.endsWith("/")) url else "$url/"
            else -> "http://" + (if (url.endsWith("/")) url else "$url/")
        }
        
        if (baseUrl.isNotEmpty()) {
            sharedPrefs.edit().putString("base_url", baseUrl).apply()
            _savedUrl.value = baseUrl
            setupApiService(baseUrl)
            refreshStatus()
        }
    }

    private fun setupApiService(baseUrl: String) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            apiService = retrofit.create(ApiService::class.java)
        } catch (e: Exception) {
            _error.value = "Invalid URL: ${e.message}"
            apiService = null
        }
    }

    fun refreshStatus() {
        val service = apiService ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.getStatus()
                if (response.isSuccessful) {
                    _status.value = response.body() ?: emptyMap()
                } else {
                    _error.value = "Fetch failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun triggerSync() {
        val service = apiService ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = service.syncAll()
                if (response.isSuccessful) {
                    refreshStatus()
                } else {
                    _error.value = "Sync failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sync error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
