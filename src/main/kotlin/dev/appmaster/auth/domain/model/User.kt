package dev.appmaster.auth.domain.model

import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime

data class User(
    val _id: Id<User>? = newId(),
    val name: String,
    val email: String,
    val password: String,
    val createDate: LocalDateTime = LocalDateTime.now()
)
