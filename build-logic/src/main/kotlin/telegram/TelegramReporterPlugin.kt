package telegram

import TelegramApi
import TelegramReporterTask
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import gradle.kotlin.dsl.accessors._ece9ecfc410f73c47b8fc5a50c6254ee.android
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.create
import validation.ValidateApkSizeTask
import java.io.File

class TelegramReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("Android not found")

        val extension = project.extensions.create("tgReporter", TelegramExtension::class)
        val telegramApi = TelegramApi(HttpClient(OkHttp))
        androidComponents.onVariants { variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)

            val sizeCheckEnabled = extension.checkSize.get()
            var taskSize: String? = null
            val file = File("apkFileSize.txt")
            println(file.path)

            if (sizeCheckEnabled) {
                project.tasks.register(
                    "validateApkSizeFor${variant.name.capitalized()}",
                    ValidateApkSizeTask::class.java,
                    telegramApi
                ).configure {
                    apkDir.set(artifacts)
                    sizeN.set(extension.setSizeLimitMB)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                    sizeFile.set(File("build/apkFileSize.txt"))
                }
            }

            project.tasks.register(
                "reportTelegramApkFor${variant.name.capitalized()}",
                TelegramReporterTask::class.java,
                telegramApi
            ).configure {
                if (sizeCheckEnabled) {
                    val task: ValidateApkSizeTask =
                        project.tasks.findByName(
                            "validateApkSizeFor${variant.name.capitalized()}"
                        ) as ValidateApkSizeTask
                    taskSize = task.sizeFile.get().asFile.readText() + " MB"
                }
                apkDir.set(artifacts)
                apkSize.set(taskSize ?: "unspecified")
                variantName.set(variant.name)
                versionCode.set(
                    project.android.defaultConfig.versionCode?.toString() ?: "unspecified"
                )
                token.set(extension.token)
                chatId.set(extension.chatId)
            }

        }
    }
}

interface TelegramExtension {
    val checkSize: Property<Boolean>
    val setSizeLimitMB: Property<Int>
    val chatId: Property<String>
    val token: Property<String>
}