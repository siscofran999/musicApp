package com.sisco.musicapp.data

import com.sisco.musicapp.data.model.*
import com.sisco.musicapp.data.remote.RestApi
import io.reactivex.Flowable

class RemoteDataSource(private val restApi: RestApi) {
    fun fetchToken(request: RequestToken): Flowable<ResponseToken> {
        return restApi.createGetToken(
            baseUrl = "https://accounts.spotify.com/",
            request = request
        )
    }

    fun fetchList(token: String): Flowable<ResponseList> {
        return restApi.getList(
            baseUrl = "https://api.spotify.com/v1/",
            token = token
        )
    }

    fun searchArtist(token: String, query: String): Flowable<ResponseArtists> {
        return restApi.searchArtist(
            baseUrl = "https://api.spotify.com/v1/",
            token = token,
            query = query
        )
    }
}