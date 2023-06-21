package com.sisco.musicapp.data.repository

import com.sisco.musicapp.data.model.RequestToken
import com.sisco.musicapp.data.model.ResponseArtists
import com.sisco.musicapp.data.model.ResponseList
import com.sisco.musicapp.data.model.ResponseToken
import io.reactivex.Flowable

interface MainRepository {
    fun fetchToken(request: RequestToken): Flowable<ResponseToken>
    fun fetchList(token: String): Flowable<ResponseList>
    fun searchArtist(token: String, query: String): Flowable<ResponseArtists>
}