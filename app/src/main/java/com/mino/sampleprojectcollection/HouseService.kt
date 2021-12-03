package com.mino.sampleprojectcollection

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/82c7009c-cfb8-4457-ba43-a6eb5878a9e8")
    fun getHouseList(): Call<HouseDto>
}