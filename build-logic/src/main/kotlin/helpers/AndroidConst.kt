package helpers

import org.gradle.api.JavaVersion

object AndroidConst {
    const val NAMESPACE = "com.example.todoapp"
    const val COMPILE_SKD = 34
    const val TARGET_SKD = 34
    const val MIN_SKD = 24
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_17
    const val KOTLIN_JVM_TARGET = "17"
}