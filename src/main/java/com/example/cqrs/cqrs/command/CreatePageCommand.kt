package com.example.cqrs.cqrs.command

data class CreatePageCommand constructor(
        val id: Long?,
        val title: String?,
        val author: String? ,
):Command
