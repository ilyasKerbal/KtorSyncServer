package dev.appmaster.auth.data.encryption

import dev.appmaster.core.config.SecretConfig
import io.ktor.util.*
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter

class HashImpl(secretConfig: SecretConfig): Hash {

    private val digest = SHA256Digest()
    private val hmac = HMac(digest)

    init {
        hmac.init(KeyParameter(secretConfig.secretKey.toByteArray()))
    }

    override fun hash(data: String): String {
        val dataBytes = data.toByteArray(Charsets.UTF_8)
        hmac.update(dataBytes, 0, dataBytes.size)
        val result = ByteArray(hmac.macSize)
        hmac.doFinal(result, 0)
        return result.encodeBase64()
    }
}