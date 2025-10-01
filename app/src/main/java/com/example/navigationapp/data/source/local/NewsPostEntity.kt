package com.example.navigationapp.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_posts")
class NewsPostEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "author") val authorName: String,
    @ColumnInfo(name = "published_time") val publishedDate: Long,
    @ColumnInfo(name = "other") val othersJson: String
)