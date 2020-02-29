package fr.legrand.runalytics.data.component

import android.os.Environment
import timber.log.Timber
import java.io.File

private const val SESSION_LOG_FILE_FORMAT = "runalytics/session_%d.log"
private const val SESSION_LOG_ERROR_FORMAT = "ERROR : %s\n"
private const val SESSION_LOG_INFO_FORMAT = "INFO : %s\n"

class LogComponentImpl : LogComponent {

    private var currentSessionLogFile: File? = null

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun i(message: String?) {
        currentSessionLogFile?.appendText(SESSION_LOG_INFO_FORMAT.format(message))
        Timber.i(message)
    }

    override fun e(message: String?) {
        currentSessionLogFile?.appendText(SESSION_LOG_ERROR_FORMAT.format(message))
        Timber.e(message)
    }

    override fun startSession(id: Long) {
        i("Opening session file $id")
        currentSessionLogFile = File(
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/${SESSION_LOG_FILE_FORMAT.format(
                id
            )}"
        )
        if (!currentSessionLogFile!!.exists()) {
            currentSessionLogFile!!.parentFile.mkdirs()
            currentSessionLogFile!!.createNewFile()
        }
    }

    override fun endSession(id: Long) {
        i("Closing session file $id")
        currentSessionLogFile = null
    }
}