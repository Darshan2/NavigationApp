package com.example.navigationapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.navigationapp.data.model.NewsPost
import com.example.navigationapp.data.model.NewsPostUi


@Dao
interface NewsPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(posts: List<NewsPostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsPost(posts: NewsPostEntity)

    @Query("DELETE FROM news_posts WHERE id = :postId")
    suspend fun deleteNewsPost(postId: Int): Int

    /**
     * Delete all NewsPost.
     */
    @Query("DELETE FROM news_posts")
    suspend fun deleteAllNewsPosts()

    @Query("SELECT * FROM news_posts")
    suspend fun getAllNewsPosts(): List<NewsPostEntity>

    @Query("SELECT id, type, title, content  FROM news_posts ORDER BY id DESC LIMIT :limit OFFSET :offSet")
    suspend fun getNewsPosts(offSet: Int, limit: Int): List<NewsPostUi>

    @Query("SELECT id, type, title, content FROM news_posts WHERE id = :postId")
    suspend fun getNewsPostUiModel(postId: Int): NewsPostUi

    @Query("SELECT * FROM news_posts WHERE id = :postId")
    suspend fun getNewsPost(postId: Int): NewsPostEntity
}