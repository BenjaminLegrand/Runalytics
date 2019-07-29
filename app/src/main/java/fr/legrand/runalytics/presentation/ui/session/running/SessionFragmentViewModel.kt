package fr.legrand.runalytics.presentation.ui.session.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.ui.session.running.item.RALocationViewDataWrapper
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SessionFragmentViewModel(private val locationRepository: LocationRepository) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val sessionSaved = SingleLiveEvent<Unit>()
    val traveledDistanceLiveData = MutableLiveData<RALocationViewDataWrapper>()

    override fun onCleared() {
        disposable.clear()
    }

    fun startLocationComputation() {
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

    fun stopLocationComputation() {
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