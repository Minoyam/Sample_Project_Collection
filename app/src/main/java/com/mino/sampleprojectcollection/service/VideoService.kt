package com.mino.sampleprojectcollection.service

import com.mino.sampleprojectcollection.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/2cf1b4d0-6e55-4426-8654-3fa905857f87")
    fun listVideos() : Call<VideoDto>
}