package dev.appmaster.auth.data.encryption

interface Hash {
    fun hash(data: String): String
}