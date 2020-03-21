package fr.legrand.runalytics.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.component.notification.NotificationComponent
import fr.legrand.runalytics.presentation.di.InjectionValues
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

private const val FOREGROUND_ID = 15253

class SessionComputationService : Service() {

    private val notificationComponent: NotificationComponent by inject()
    private val locationRepository: LocationRepository by inject()
    private val sessionPublish: PublishSubject<Pair<SessionState, Session>> by inject(
        named(
            InjectionValues.SESSION_PUBLISH
        )
    )
    private val sessionTimerPublish: PublishSubject<Long> by inject(named(InjectionValues.SESSION_TIMER_PUBLISH))

    private val sessionCompositeDisposable = CompositeDisposable()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(FOREGROUND_ID, notificationComponent.getNewSessionNotification())
        locationRepository.startLocationComputation().subscribeOn(Schedulers.io()).subscribeBy(
            onNext = {
                sessionPublish.onNext(it)
            },
            onError = {

            }
        ).addToComposite(sessionCompositeDisposable)

        locationRepository.startSessionTimer().subscribeOn(Schedulers.io()).subscribeBy(
            onNext = {
                sessionTimerPublish.onNext(it)
                notificationComponent.updateSessionNotification(
                    notificationId = FOREGROUND_ID,
                    timer = it
                )
            }, onError = {

            }

        ).addToComposite(sessionCompositeDisposable)

        return START_STICKY
    }

    override fun onDestroy() {
        locationRepository.stopLocationComputation()
        sessionCompositeDisposable.clear()
        stopForeground(true)
        super.onDestroy()
    }
}