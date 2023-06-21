package com.sisco.musicapp.data.repository

import com.sisco.musicapp.data.RemoteDataSource
import com.sisco.musicapp.data.model.RequestToken
import com.sisco.musicapp.data.model.ResponseArtists
import com.sisco.musicapp.data.model.ResponseList
import com.sisco.musicapp.data.model.ResponseToken
import io.reactivex.Flowable

class MainRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : MainRepository {

    override fun fetchToken(request: RequestToken): Flowable<ResponseToken> {
        return remoteDataSource.fetchToken(request)
    }

    override fun fetchList(token: String): Flowable<ResponseList> {
        return remoteDataSource.fetchList(token)
    }

    override fun searchArtist(token: String, query: String): Flowable<ResponseArtists> {
        return remoteDataSource.searchArtist(token, query)
    }
}