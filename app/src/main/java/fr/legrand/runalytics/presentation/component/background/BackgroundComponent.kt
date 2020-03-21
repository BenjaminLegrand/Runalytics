package fr.legrand.runalytics.presentation.component.background

import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.model.SessionState
import io.reactivex.Observable

interface BackgroundComponent {
    fun observeSession() : Observable<Pair<SessionState, Session>>
    fun observeSessionTimer() : Observable<Long>
    fun startSession()
    fun stopSession()
}