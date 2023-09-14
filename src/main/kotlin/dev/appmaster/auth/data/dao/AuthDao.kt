package dev.appmaster.auth.data.dao

import dev.appmaster.auth.data.encryption.Hash
import dev.appmaster.auth.data.entity.EntityDevice
import dev.appmaster.auth.data.entity.EntityUser
import dev.appmaster.auth.data.tables.Devices
import dev.appmaster.auth.data.tables.Users
import dev.appmaster.auth.external.request.SignupRequest
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface AuthDao {
    fun isEmailTaken(email: String): Boolean

    fun addNewUser(signupRequest: SignupRequest): String

    fun getDeviceById(deviceId: String): EntityDevice?

    fun getUserByEmailAndPassword(email: String, password: String): EntityUser?

    fun addDevice(userId: UUID, notificationId: String, deviceName: String, deviceBrand: String): String

    fun removeDevice(deviceId: String): Boolean

    fun getUserDeices(currentDevice: String): List<String>

    fun getUserFromDevice(deviceId: String): EntityUser?
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

    override fun getUserByEmailAndPassword(email: String, password: String): EntityUser? = transaction {
        val hashedPassword = hash.hash(password)
        EntityUser.find {
            (Users.email eq email) and (Users.password eq hashedPassword)
        }.firstOrNull()
    }

    override fun addDevice(userId: UUID, notificationId: String, deviceName: String, deviceBrand: String): String = transaction {
        EntityDevice.new {
            this.user = EntityUser[userId]
            this.notificationId = notificationId
            this.deviceName = deviceName
            this.deviceBrand = deviceBrand
        }.id.value.toString()
    }

    override fun removeDevice(deviceId: String): Boolean = transaction {
        val device = EntityDevice.findById(UUID.fromString(deviceId))
        device?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun getUserDeices(currentDevice: String): List<String> = transaction {
        val device = EntityDevice[UUID.fromString(currentDevice)]
        EntityDevice.find {
            Devices.user eq device.user.id
        }.map {
            it.id.value.toString()
        }
    }

    override fun getUserFromDevice(deviceId: String): EntityUser? = transaction {
        val device = EntityDevice.findById(UUID.fromString(deviceId)) ?: return@transaction null
        EntityUser.findById(device.user.id)
    }
}