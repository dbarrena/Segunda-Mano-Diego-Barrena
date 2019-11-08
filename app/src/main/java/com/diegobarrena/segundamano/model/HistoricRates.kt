package com.diegobarrena.segundamano.model

import com.google.gson.annotations.SerializedName

data class HistoricRates(
    @SerializedName("start_at")
    val startAt: String,
    @SerializedName("base")
    val base: String,
    @SerializedName("end_at")
    val endAt: String,
    @SerializedName("rates")
    val rates: HashMap<String, Rate>
)