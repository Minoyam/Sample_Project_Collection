package com.mino.sampleprojectcollection.home

data class ArticleModel(
    val sellerId : String = "",
    val title : String = "",
    val createdAt : Long = 0L,
    val price : String = "",
    val imageUrl : String = ""
)