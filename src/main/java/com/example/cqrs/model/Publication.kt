package com.example.cqrs.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "publication")
class Publication constructor(

        @field:Id
        @field:GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
        val id: Long?,
        val title: String?,
        val author: String? ,
){
    data class Builder(
        var id: Long? = null,
        var title: String? = null,
        var author: String? = null,
    ) {
        fun build(): Publication {
            return Publication(
                id = id,
                title = title,
                author = author,
            )
        }
    }
}