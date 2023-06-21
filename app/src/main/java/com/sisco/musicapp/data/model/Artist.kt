package com.sisco.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class RequestArtist(
    @SerializedName("token")
    val token: String,
    @SerializedName("query")
    val query: String
)

data class ResponseArtists(
    @SerializedName("artists")
    val artists: Artists
)

data class Artists(
    @SerializedName("items")
    val items: List<ItemArtist>
)

data class ItemArtist(
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val image: List<Image> ?= null,
    @SerializedName("name")
    val name: String
)