package fr.legrand.runalytics.presentation.component.error

import android.content.Context
import fr.legrand.runalytics.R
import fr.legrand.runalytics.data.exception.EmptySessionException

class ErrorTranslater constructor(private val context: Context) {

    fun translate(throwable: Throwable): String {
        return when (throwable) {
            is EmptySessionException -> context.getString(R.string.empty_session_error)
            else -> context.getString(R.string.default_text_error)
        }
    }

}