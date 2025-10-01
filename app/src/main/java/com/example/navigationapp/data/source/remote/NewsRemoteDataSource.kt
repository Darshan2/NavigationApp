package com.example.navigationapp.data.source.remote

import com.example.navigationapp.data.model.NewsPostUi
import com.example.navigationapp.data.model.toUiModel
import com.example.navigationapp.data.source.NewsDataSource
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService
): NewsDataSource {

    override suspend fun getNewsList(pageNum: Int, pageSize: Int): List<NewsPostUi>? {
       return newsApiService.getNewsList(pageNum)?.toUiModel()
    }

    override suspend fun getNewsPost(postId: Int): NewsPostUi? {
        return  newsApiService.getNewsPost(postId = postId)?.toUiModel()
    }

    override suspend fun refreshNewsFeed() {
        TODO("Not yet implemented")
    }
}

