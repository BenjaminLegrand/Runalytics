package fr.legrand.runalytics.presentation.ui.session.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.ui.session.running.item.RALocationViewDataWrapper
import fr.legrand.runalytics.presentation.utils.TimeUtils
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SessionFragmentViewModel(
    private val locationRepository: LocationRepository
) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val sessionSaved = SingleLiveEvent<Unit>()
    val traveledDistanceLiveData = MutableLiveData<RALocationViewDataWrapper>()
    val sessionTimer = MutableLiveData<Triple<Long, Long, Long>>()

    override fun onCleared() {
        disposable.clear()
    }

    fun startLocationComputation() {
        startSessionTimer()
        locationRepository.startLocationComputation().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onNext = {
                traveledDistanceLiveData.postValue(
                    RALocationViewDataWrapper(
                        it
                    )
                )
            }
        ).addToComposite(disposable)
    }

    private fun startSessionTimer() {
        locationRepository.startSessionTimer().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onNext = {
                sessionTimer.postValue(TimeUtils.extractTime(it))
            }
        ).addToComposite(disposable)
    }

    fun stopLocationComputation() {
        disposable.clear()
        locationRepository.stopLocationComputation().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onComplete = {}
        ).addToComposite(disposable)
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
}