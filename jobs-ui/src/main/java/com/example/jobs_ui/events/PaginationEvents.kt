package com.example.jobs_ui.events

interface PaginationEvents {
    fun loadNextPage()
    fun refreshPage()
}

fun defaultPaginationEvents() = object : PaginationEvents {
    override fun loadNextPage() {}
    override fun refreshPage() {}
}