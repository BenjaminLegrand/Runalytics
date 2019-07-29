package fr.legrand.runalytics.presentation.ui.session.list.item

import fr.legrand.runalytics.data.model.Session

class SessionViewDataWrapper(
    private val session: Session
) {
    fun getDuration(): String = session.locations.lastOrNull()?.timestamp?.toString() ?: ""
}