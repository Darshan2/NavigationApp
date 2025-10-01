package com.example.navigationapp.data.model

import android.os.Parcelable
import com.example.navigationapp.data.source.local.NewsPostEntity
import com.google.gson.JsonObject
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class NewsPost(
    val id: Int,
    val type: Int,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: Long,
    val additionalField1: String, // To simulate some of other fields
    val additionalField2: Int,
): Parcelable

@Parcelize
data class NewsPostUi(
    val id: Int,
    val type: Int,
    val title: String,
    val content: String
): Parcelable

fun NewsPost.toUiModel(): NewsPostUi {
    return NewsPostUi(
        id = id,
        type = type,
        title = title,
        content = content
    )
}

fun List<NewsPost>.toUiModel(): List<NewsPostUi> {
    val list = mutableListOf<NewsPostUi>()
    for(item in this) {
        list.add(item.toUiModel())
    }
    return list
}

fun NewsPost.toRoomModel(): NewsPostEntity {
    return NewsPostEntity(
        id = id,
        type = type,
        title = title,
        content = content,
        authorName = author,
        publishedDate = publishDate,
        othersJson = JSONObject().apply {
            put("additionalField1", additionalField1)
            put("additionalField2", additionalField2)
        }.toString()
    )
}