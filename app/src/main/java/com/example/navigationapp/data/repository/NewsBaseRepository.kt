package com.example.navigationapp.data.repository

import com.example.common_core.utils.Result
import com.example.navigationapp.data.model.NewsPostUi
import kotlinx.coroutines.flow.Flow

interface NewsBaseRepository {
    val pageSie: Int

    suspend fun getNewsList(pageNum: Int): Flow<Result<List<NewsPostUi>?>>

    suspend fun getNewsPost(postId: Int): Flow<Result<NewsPostUi?>>
}