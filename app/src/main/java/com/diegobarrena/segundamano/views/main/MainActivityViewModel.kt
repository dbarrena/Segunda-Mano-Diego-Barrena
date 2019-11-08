package com.diegobarrena.segundamano.views.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegobarrena.segundamano.api.ExchangeRatesApi
import com.diegobarrena.segundamano.model.HistoricRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class MainActivityViewModel @Inject
constructor(
    private val exchangeRatesApi: ExchangeRatesApi
) : ViewModel() {
    val historicRates: MutableLiveData<HistoricRates> by lazy {
        MutableLiveData<HistoricRates>().apply { setValue(null) }
    }

    val messages: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { setValue(null) }
    }

    fun getHistoricRates(startDate: String, endDate: String) {
        exchangeRatesApi.getHistoricRates(startDate, endDate)
            .enqueue(object : Callback<HistoricRates> {
                override fun onResponse(
                    call: Call<HistoricRates>,
                    response: Response<HistoricRates>
                ) {
                    if(response.isSuccessful) {
                        if (response.code() == 200) {
                            response.body()?.let {
                                historicRates.postValue(it)
                            } ?: run {
                                messages.postValue(
                                    "There are no records for this time frame."
                                )
                            }
                        } else {
                            Timber.e("Error code ${response.code()} at ${call.request().url()}")
                            messages.postValue("There was an error processing your request.")
                        }
                    } else {
                        Timber.e("Error: ${response.raw().message()}")
                        messages.postValue(
                            "There was an error processing your request"
                        )
                    }
                }

                override fun onFailure(call: Call<HistoricRates>, t: Throwable) {
                    Timber.e("Error: ${t.localizedMessage}")
                    messages.postValue("There was an error processing your request.")
                }

            })
    }
}