package com.example.navigationapp.data.source

import com.example.navigationapp.data.model.NewsPostUi

interface NewsDataSource {
    suspend fun getNewsList(pageNum: Int, pageSize: Int): List<NewsPostUi>?

    suspend fun getNewsPost(postId: Int): NewsPostUi?

    suspend fun refreshNewsFeed()
}