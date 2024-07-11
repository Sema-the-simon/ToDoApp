plugins {
    id("android-app-convention")
    id("todoapp.hilt")
    kotlin("plugin.serialization")
    id("telegram-reporter")
}

tgReporter {
    checkSize = true
    setSizeLimitMB = 5 * 1024 * 1024
    token = providers.environmentVariable("TG_TOKEN")
    chatId = providers.environmentVariable("TG_CHAT")
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
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    //serializable
    implementation(libs.kotlinx.serialization.json)
}