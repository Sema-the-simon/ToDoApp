package helpers

import org.gradle.api.JavaVersion

object AndroidConst {
    const val NAMESPACE = "com.example.todoapp"
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 24
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_17
    const val KOTLIN_JVM_TARGET = "17"
    const val APK_SIZE = 5 * 1024 * 1024
}