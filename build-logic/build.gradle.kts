plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("hilt") {
            id = "todoapp.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("telegram-reporter") {
            id = "telegram-reporter"
            implementationClass = "plugins.TelegramReporterPlugin"
        }
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.kotlin.serialization.gradle.plugin)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

