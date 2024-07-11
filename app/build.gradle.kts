plugins {
    id("android-app-convention")
    id("todoapp.hilt")
    kotlin("plugin.serialization")
    id("telegram-reporter")
}

tgReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
}

android {
    defaultConfig {
        applicationId = "com.example.todoapp"
        versionCode = 1
        versionName = "1.0"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    //serializable
    implementation(libs.kotlinx.serialization.json)
}