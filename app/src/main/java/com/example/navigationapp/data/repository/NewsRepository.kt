package com.example.navigationapp.data.repository

import com.example.common_core.utils.Result
import com.example.navigationapp.data.model.NewsPostUi
import com.example.common_core.utils.safeApiCall
import com.example.navigationapp.data.source.NewsDataSource
import com.example.common_core.di.qualifiers.NewsLocalDataSource
import com.example.common_core.di.qualifiers.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    @NewsRemoteDataSource private val remoteDataSource: NewsDataSource,
    @NewsLocalDataSource private val localDataSource: NewsDataSource
): NewsBaseRepository {

    override val pageSie: Int
        get() = 10

    override suspend fun getNewsList(pageNum: Int): Flow<Result<List<NewsPostUi>?>> {
        return safeApiCall {
            remoteDataSource.getNewsList(pageNum, pageSie)
        }
    }

    override suspend fun getNewsPost(postId: Int): Flow<Result<NewsPostUi?>> {
        return safeApiCall {
            remoteDataSource.getNewsPost(postId)
        }
    }


}