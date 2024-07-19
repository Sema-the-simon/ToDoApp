package helpers
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.File

fun BaseAppModuleExtension.baseAndroidConfig() {
    namespace = AndroidConst.NAMESPACE
    compileSdk = AndroidConst.COMPILE_SDK
    defaultConfig {

        minSdk = AndroidConst.MIN_SDK
        targetSdk = AndroidConst.TARGET_SDK

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
fun Double.format(digits: Int) = "%.${digits}f".format(this)
fun File.sizeBytes() = if (!exists()) 0.0 else length().toDouble()
fun File.sizeKb() = sizeBytes() / 1024
fun File.sizeMb() = sizeKb() / 1024