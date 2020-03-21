package fr.legrand.runalytics.presentation.component.error

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import fr.legrand.runalytics.R
import timber.log.Timber

class ErrorDisplayComponentSnackbar
constructor(
    private val errorTranslater: ErrorTranslater
) : ErrorDisplayComponent {

    override fun displayError(activity: Activity, throwable: Throwable) {
        Snackbar.make(
            activity.findViewById<View>(android.R.id.content),
            errorTranslater.translate(throwable),
            Snackbar.LENGTH_SHORT
        ).apply {
            view.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.snackbar_error_background
                )
            )
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                .setTextColor(ContextCompat.getColor(activity, R.color.white))
            show()
        }
        Timber.e(throwable)
    }

}