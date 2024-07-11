package helpers
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.File

fun BaseAppModuleExtension.baseAndroidConfig() {
    namespace = AndroidConst.NAMESPACE
    compileSdk = AndroidConst.COMPILE_SKD
    defaultConfig {
        minSdk = AndroidConst.MIN_SKD
        targetSdk = AndroidConst.TARGET_SKD

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConst.COMPILE_JDK_VERSION
        targetCompatibility = AndroidConst.COMPILE_JDK_VERSION
    }
    kotlinOptions {
        jvmTarget = AndroidConst.KOTLIN_JVM_TARGET
    }
}

fun File.sizeBytes() = if (!exists()) 0.0 else length().toDouble()
fun File.sizeKb() = sizeBytes() / 1024
fun File.sizeMb() = sizeKb() / 1024