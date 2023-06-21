package com.sisco.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class ResponseList(
    @SerializedName("tracks")
    val tracks: Tracks
)

data class Tracks(
    @SerializedName("items")
    val items: List<ItemTrack>
)

data class ItemTrack(
    @SerializedName("added_at")
    val added_at: String,
    @SerializedName("track")
    val track: Track
)

data class Track(
    @SerializedName("name") // Title Song
    val name: String,
    @SerializedName("album")
    val album: Album,
    @SerializedName("artists")
    val artists: List<Artist>,
    @SerializedName("preview_url")
    val preview_url: String? = null
)

data class Album(
    @SerializedName("name") // Album Name
    val name: String,
    @SerializedName("images")
    val images: List<Image>
)

data class Image(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)

data class Artist(
    @SerializedName("name") // Artist Name
    val name: String
)