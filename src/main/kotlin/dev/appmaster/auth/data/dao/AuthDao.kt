package dev.appmaster.auth.data.dao

import dev.appmaster.auth.data.encryption.Hash
import dev.appmaster.auth.data.entity.EntityDevice
import dev.appmaster.auth.data.entity.EntityUser
import dev.appmaster.auth.data.tables.Users
import dev.appmaster.auth.external.SignupRequest
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface AuthDao {
    fun isEmailTaken(email: String): Boolean

    fun addNewUser(signupRequest: SignupRequest): String

    fun getDeviceById(deviceId: String): EntityDevice?
}

class AuthDaoImpl(
    private val hash: Hash
): AuthDao {

    override fun isEmailTaken(email: String): Boolean = transaction {
        EntityUser.find {
            Users.email eq email
        }.firstOrNull() != null
    }

    override fun addNewUser(signupRequest: SignupRequest): String = transaction {
        val userId = EntityUser.new {
            this.name = signupRequest.name
            this.email = signupRequest.email
            this.password = hash.hash(signupRequest.password)
        }.id.value

        EntityDevice.new {
            this.user = EntityUser[userId]
            this.deviceBrand = signupRequest.deviceBrand
            this.deviceName = signupRequest.deviceName
            this.notificationId = signupRequest.notificationId
        }.id.value.toString()
    }

    override fun getDeviceById(deviceId: String): EntityDevice? = transaction {
        EntityDevice.findById(UUID.fromString(deviceId))
    }
}