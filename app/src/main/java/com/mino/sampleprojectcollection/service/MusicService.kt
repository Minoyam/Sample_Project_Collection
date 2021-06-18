package com.mino.sampleprojectcollection.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/ef8bf322-38de-4bf3-8fc3-bd9cd83b4eca")
    fun listMusics(): Call<MusicDto>
}