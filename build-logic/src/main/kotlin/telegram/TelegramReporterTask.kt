import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val apkSize: Property<String>

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
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {

                val newName = "${it.parent}/todolist-${variantName.get()}-${versionCode.get()}.apk"
                val nFile = File(newName)
                it.renameTo(nFile)

                runBlocking {
                    telegramApi.sendMessage("Build finished. Apk size - ${apkSize.get()}", token, chatId).apply {
                        println("Status = $status")
                    }
                }
                runBlocking {
                    telegramApi.upload(it, token, chatId).apply {
                        println("Status = $status")
                    }
                }
            }
    }
}