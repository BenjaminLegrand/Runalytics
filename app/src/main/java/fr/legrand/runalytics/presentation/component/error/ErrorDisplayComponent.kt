package fr.legrand.runalytics.presentation.component.error

import android.app.Activity

interface ErrorDisplayComponent{
    fun displayError(activity : Activity, throwable : Throwable)
}