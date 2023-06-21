package com.sisco.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class RequestToken(
    @SerializedName("grant_type")
    val grant_type: String = "",
    @SerializedName("client_id")
    val client_id: String = "",
    @SerializedName("client_secret")
    val client_secret: String = ""
)

data class ResponseToken(
    @SerializedName("access_token")
    val access_token: String = "",
    @SerializedName("token_type")
    val token_type: String = "",
    @SerializedName("expires_in")
    val expires_in: String = ""
)
