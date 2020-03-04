package fr.legrand.runalytics.presentation.ui.session.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.component.background.BackgroundComponent
import fr.legrand.runalytics.presentation.ui.session.list.item.SessionViewDataWrapper
import fr.legrand.runalytics.presentation.utils.TimeUtils
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlin.math.max


private const val M_TO_KM = 1000

class SessionFragmentViewModel(
    private val backgroundComponent: BackgroundComponent,
    private val locationRepository: LocationRepository
) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val sessionSaved = SingleLiveEvent<Unit>()
    val sessionUpdate = MutableLiveData<SessionViewDataWrapper>()
    val sessionState = MutableLiveData<SessionState>()
    val sessionTimer = MutableLiveData<Triple<Long, Long, Long>>()
    val traveledDistance = MutableLiveData<Float>()
    val currentKmTime = MutableLiveData<Long>()
    val lastKmTime = MutableLiveData<Long>()

    init {
        observeSession()
        observeSessionTimer()
    }

    override fun onCleared() {
        backgroundComponent.stopSession()
        disposable.clear()
    }

    fun startSession() {
        backgroundComponent.startSession()
    }

    fun stopLocationComputation() {
        backgroundComponent.stopSession()
    }

    fun saveCurrentSession() {
        locationRepository.saveCurrentSession().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onComplete = {
                sessionSaved.call()
            }
        ).addToComposite(disposable)
    }

    private fun observeSession() {
        backgroundComponent.observeSession().subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onNext = {
                sessionState.postValue(it.first)
                if (it.first == SessionState.VALID) {
                    val session = it.second
                    val wrapper = SessionViewDataWrapper(session)
                    sessionUpdate.postValue(wrapper)
                    traveledDistance.postValue(session.traveledDistance / M_TO_KM)
                    session.kmTimeList.lastOrNull()?.let {
                        currentKmTime.postValue(it)
                    }
                    session.kmTimeList.take(max(session.kmTimeList.size - 1, 0)).lastOrNull()?.let {
                        lastKmTime.postValue(it)
                    }
                }
            }
        ).addToComposite(disposable)
    }

    private fun observeSessionTimer() {
        backgroundComponent.observeSessionTimer().subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onNext = {
                sessionTimer.postValue(TimeUtils.extractTime(it))
            }
        ).addToComposite(disposable)
    }

}