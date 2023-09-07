@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.diffplug.spotless)
}

group = "dev.appmaster"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude(".idea/")
            ktlint(libs.versions.ktlint.toString())
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.compression.jvm)
    implementation(libs.ktor.server.host.common.jvm)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.http.redirect.jvm)
    implementation(libs.ktor.server.auth.jvm)
    implementation(libs.ktor.auth.jwt)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.logback.classic)

    // Test
    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
}
