package com.diegobarrena.segundamano.model

import com.google.gson.annotations.SerializedName

data class Rate(
    @SerializedName("USD")
    val dollar: Float
)