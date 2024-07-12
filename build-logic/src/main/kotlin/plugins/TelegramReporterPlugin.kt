package plugins

import api.TelegramApi
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import gradle.kotlin.dsl.accessors._ece9ecfc410f73c47b8fc5a50c6254ee.android
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import tasks.ApkReportTask
import tasks.TelegramReporterTask
import tasks.ValidateApkSizeTask
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


            val buildDir = "${project.layout.buildDirectory.get()}/apkStatistic"
            val sizeCheckEnabled = extension.checkSize.get()
            val validationTask =
                if (sizeCheckEnabled)
                    project.tasks.register<ValidateApkSizeTask>(
                        "validateApkSizeFor${variant.name.capitalized()}",
                        telegramApi,
                    ).apply {
                        configure {
                            apkDir.set(artifacts)
                            sizeN.set(extension.setSizeLimitMB)
                            token.set(extension.token)
                            chatId.set(extension.chatId)
                            sizeFile.set(File("$buildDir/apkFileSize.txt"))
                        }
                    } else null

            val reportTgTask = project.tasks.register<TelegramReporterTask>(
                "reportTelegramApkFor${variant.name.capitalized()}",
                telegramApi
            ).apply {
                configure {
                    apkDir.set(artifacts)
                    val taskFile = validationTask?.get()?.sizeFile
                    if (taskFile != null)
                        apkSize.set(taskFile)
                    variantName.set(variant.name)
                    versionCode.set(
                        project.android.defaultConfig.versionCode?.toString() ?: "unspecified"
                    )
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
            }

            val apkReportTask = project.tasks.register<ApkReportTask>(
                "sendApkStatisticFor${variant.name.capitalized()}",
                telegramApi
            ).apply {
                configure {
                    apkDir.set(artifacts)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
                dependsOn(reportTgTask)
            }
        }
    }
}

interface TelegramExtension {
    val checkSize: Property<Boolean>
    val setSizeLimitMB: Property<Int>
    val sendReport: Property<Boolean>
    val chatId: Property<String>
    val token: Property<String>
}