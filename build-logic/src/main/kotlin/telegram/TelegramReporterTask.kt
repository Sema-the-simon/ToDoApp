import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:InputFile
    @get:Optional
    abstract val apkSize: RegularFileProperty

    @get:Input
    abstract val variantName: Property<String>

    @get:Input
    abstract val versionCode: Property<String>

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()
        val size = try {
            apkSize.get().asFile.readText()
        } catch (e: Exception) {
            "unspecified"
        }
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {

                val newName = "${it.parent}/todolist-${variantName.get()}-${versionCode.get()}.apk"
                val nFile = File(newName)
                it.renameTo(nFile)

                runBlocking {
                    telegramApi.sendMessage("Build finished. Apk size - $size", token, chatId)
                        .apply {
                            println("Status = $status")
                        }
                }
                runBlocking {
                    telegramApi.upload(nFile, token, chatId).apply {
                        println("Status = $status")
                    }
                }
            }
    }
}