package com.diegobarrena.segundamano.api

import com.diegobarrena.segundamano.model.HistoricRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApi {
    @GET("history?")
    fun getHistoricRates(@Query("start_at") startDate: String,
                         @Query("end_at") endDate: String,
                         @Query("symbols") symbols: String = "USD",
                         @Query("base") base: String = "EUR"): Call<HistoricRates>
}