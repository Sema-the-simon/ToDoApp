import helpers.AndroidConst

plugins {
    id("android-app-convention")
    id("todoapp.hilt")
    id("telegram-reporter")
}

tgReporter {
    checkSize = true
    sendReport = true
    setSizeLimitMB = AndroidConst.APK_SIZE
    token = providers.environmentVariable("TG_TOKEN")
    chatId = providers.environmentVariable("TG_CHAT")
}

android {
    defaultConfig {
        applicationId = "com.example.todoapp"
        versionCode = 1
        versionName = "1.0"
    }
}