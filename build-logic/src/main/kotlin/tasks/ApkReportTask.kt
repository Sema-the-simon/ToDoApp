package tasks

import api.TelegramApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.zip.ZipFile
import javax.inject.Inject

abstract class ApkReportTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()

        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { apkFike ->
                val reportFile = File("${apkFike.parent}/apk_details.txt")
                val report = analyzeApk(apkFike)
                reportFile.writeText(report)
                runBlocking {
                    telegramApi.upload(reportFile, token, chatId).apply {
                        println("Status = $status")
                    }
                }
            }
    }

    private fun analyzeApk(apkFile: File): String {
        val zipFile = ZipFile(apkFile)
        val report = StringBuilder()
        zipFile.entries().asSequence().sortedBy { it.name }.forEach { entry ->
            if (!entry.isDirectory) {
                val sizeBytes = entry.size
                val sizeKb = sizeBytes / 1024.0
                val sizeMb = sizeKb / 1024.0
                val size =
                    if (sizeMb < 1) {
                        if (sizeKb < 1)
                            "%.2f".format(sizeBytes.toDouble()) + " Byte"
                        else
                            "%.2f".format(sizeKb) + " KB"
                    } else
                        "%.2f".format(sizeMb) + " MB"
                report.append("- ${entry.name} $size\n")
            }
        }
        return report.toString()
    }
}