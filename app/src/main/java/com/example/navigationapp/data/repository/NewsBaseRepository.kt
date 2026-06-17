package com.example.navigationapp.data.repository

import com.example.common.core.utils.Result
import com.example.navigationapp.data.model.NewsPostUi
import kotlinx.coroutines.flow.Flow

interface NewsBaseRepository {
    val pageSie: Int

    fun getNewsList(pageNum: Int): Flow<Result<List<NewsPostUi>?>>

    fun getNewsPost(postId: Int): Flow<Result<NewsPostUi?>>
}