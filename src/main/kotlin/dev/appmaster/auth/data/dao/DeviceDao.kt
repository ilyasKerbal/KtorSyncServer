package dev.appmaster.auth.data.dao

import dev.appmaster.auth.domain.model.Device
import org.litote.kmongo.coroutine.CoroutineDatabase

interface DeviceDao {
    suspend fun getDeviceFromId(id: String): Device?
}

class DeviceDaoImpl(
    private val database: CoroutineDatabase
): DeviceDao {

    private val devices = database.getCollection<Device>()

    override suspend fun getDeviceFromId(id: String): Device? {
        return devices.findOneById(id)
    }
}