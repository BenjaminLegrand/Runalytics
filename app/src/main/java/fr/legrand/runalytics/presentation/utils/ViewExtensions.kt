package fr.legrand.runalytics.presentation.utils

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.setVisible(visible: Boolean, hiddenState: Int = View.GONE) {
    when {
        visible -> show()
        hiddenState == View.GONE -> hide()
        hiddenState == View.INVISIBLE -> invisible()
    }
}