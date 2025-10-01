package com.example.navigationapp.data.source.remote

import com.example.navigationapp.data.model.NewsPost
import retrofit2.http.GET

interface NewsApiService {

    @GET
    suspend fun getNewsPost(postId: Int): NewsPost?

    @GET
    suspend fun getNewsList(pageNum: Int): List<NewsPost>?

    @GET
    suspend fun searchNewsPosts(query: String): List<NewsPost>
}