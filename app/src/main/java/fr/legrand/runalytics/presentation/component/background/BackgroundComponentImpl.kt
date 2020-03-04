package fr.legrand.runalytics.presentation.component.background

import android.content.Context
import android.content.Intent
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.presentation.service.SessionComputationService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BackgroundComponentImpl constructor(
    private val context: Context,
    private val sessionPublish: PublishSubject<Pair<SessionState, Session>>,
    private val sessionTimerPublish: PublishSubject<Long>
) : BackgroundComponent {

    override fun observeSession(): Observable<Pair<SessionState, Session>> = sessionPublish

    override fun observeSessionTimer(): Observable<Long> = sessionTimerPublish

    override fun startSession() {
        context.startService(Intent(context, SessionComputationService::class.java))
    }

    override fun stopSession() {
        context.stopService(Intent(context, SessionComputationService::class.java))
    }
}