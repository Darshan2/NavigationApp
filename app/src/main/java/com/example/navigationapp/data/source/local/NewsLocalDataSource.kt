package com.example.navigationapp.data.source.local

import com.example.navigationapp.data.model.NewsPost
import com.example.navigationapp.data.model.NewsPostUi
import com.example.navigationapp.data.source.NewsDataSource
import javax.inject.Inject

class NewsLocalDataSource @Inject constructor(
    val newsPostDao: NewsPostDao
): NewsDataSource {
    override suspend fun getNewsList(pageNum: Int, pageSize: Int): List<NewsPostUi>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNewsPost(postId: Int): NewsPostUi? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshNewsFeed() {
        TODO("Not yet implemented")
    }


}