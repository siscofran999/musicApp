package com.sisco.musicapp.data.remote

import com.google.gson.Gson
import com.sisco.musicapp.data.model.RequestToken
import com.sisco.musicapp.data.model.ResponseArtists
import com.sisco.musicapp.data.model.ResponseList
import com.sisco.musicapp.data.model.ResponseToken
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class RestApi(private val restClient: RestClient) {

    fun createGetToken(baseUrl: String, request: RequestToken) : Flowable<ResponseToken> {
        return restClient.createService(baseUrl)
            .postToken(request.grant_type,request.client_id, request.client_secret)
            .subscribeOn(Schedulers.io())
            .map {
                Gson().fromJson(it, ResponseToken::class.java)
            }
    }

    fun getList(baseUrl: String, token: String): Flowable<ResponseList> {
        return restClient.createService(baseUrl)
            .getList("Bearer $token")
            .subscribeOn(Schedulers.io())
            .map {
                Gson().fromJson(it, ResponseList::class.java)
            }
    }

    fun searchArtist(baseUrl: String, token: String, query: String): Flowable<ResponseArtists> {
        return restClient.createService(baseUrl)
            .searchArtist("Bearer $token", query, "artist")
            .subscribeOn(Schedulers.io())
            .map {
                Gson().fromJson(it, ResponseArtists::class.java)
            }
    }

}