package com.diegobarrena.segundamano.views.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {
    private val pendingJobs = Job()
    override val coroutineContext: CoroutineContext
        get() = (Dispatchers.Default + pendingJobs)

    val messages: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { setValue(null) }
    }

    override fun onCleared() {
        pendingJobs.cancel()
    }
}