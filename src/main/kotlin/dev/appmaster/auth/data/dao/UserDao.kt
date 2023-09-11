package dev.appmaster.auth.data.dao

import dev.appmaster.auth.domain.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase

interface UserDao {
    suspend fun getUserByID(id: String): User?
}

class UserDaoImpl(
    private val database: CoroutineDatabase
): UserDao {

    private val users = database.getCollection<User>()

    override suspend fun getUserByID(id: String): User? {
        return users.findOneById(id)
    }
}