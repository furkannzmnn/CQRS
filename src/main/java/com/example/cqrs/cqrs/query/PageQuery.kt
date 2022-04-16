package com.example.cqrs.cqrs.query

data class PageQuery(
        val id: Long?,
        val title: String?,
        val author: String?
): Query
