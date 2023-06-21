package com.sisco.musicapp.data.remote

import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.*

interface RestService {

    @FormUrlEncoded
    @POST("api/token")
    fun postToken(
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String
    ): Flowable<JsonObject>

    @GET("playlists/37i9dQZEVXbObFQZ3JLcXt")
    fun getList(
        @Header("Authorization") token: String
    ): Flowable<JsonObject>

    @GET("search")
    fun searchArtist(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String
    ): Flowable<JsonObject>
}