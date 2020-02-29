package fr.legrand.runalytics.presentation.ui.session.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.model.RALocation
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.data.values.LocationValues
import fr.legrand.runalytics.presentation.ui.session.running.item.RALocationViewDataWrapper
import fr.legrand.runalytics.presentation.utils.LocationUtils
import fr.legrand.runalytics.presentation.utils.TimeUtils
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


private const val M_TO_KM = 1000
private const val MS_TO_S = 1000

private const val KILOMETER_VALUE = 1000

class SessionFragmentViewModel(
    private val locationRepository: LocationRepository
) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val sessionSaved = SingleLiveEvent<Unit>()
    val currentLocation = MutableLiveData<RALocationViewDataWrapper>()
    val sessionTimer = MutableLiveData<Triple<Long, Long, Long>>()
    val traveledDistance = MutableLiveData<Float>()
    val currentKmTime = MutableLiveData<Long>()
    val lastKmTime = MutableLiveData<Long>()

    private val currentSessionData = mutableListOf<RALocation>()

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
                val wrapper = RALocationViewDataWrapper(it)
                currentSessionData.add(it)
                computeCurrentSessionData()
                currentLocation.postValue(wrapper)
            }
        ).addToComposite(disposable)
    }

    private fun computeCurrentSessionData() {
        traveledDistance.postValue(currentSessionData.sumByDouble { it.lastDistance.toDouble() }.toFloat() / M_TO_KM)

        val kmSplitLocations = LocationUtils.splitByKilometer(currentSessionData)
        if (kmSplitLocations.size > 1) {
            val lastKmLocations = kmSplitLocations[kmSplitLocations.size - 2]
            if (lastKmLocations.size > 1) {
                lastKmTime.postValue((lastKmLocations.last().timestamp - lastKmLocations.first().timestamp) / MS_TO_S)
            }else{
                lastKmTime.postValue(LocationValues.NO_TIME)
            }
        }
        if (kmSplitLocations.isNotEmpty()) {
            val currentKmLocations = kmSplitLocations[kmSplitLocations.size - 1]
            if(currentKmLocations.size > 1){
                val remainingDistance =
                    KILOMETER_VALUE - currentKmLocations.sumByDouble { it.lastDistance.toDouble() }
                val remainingTime = remainingDistance / currentKmLocations.last().currentSpeed
                val elapsedTime =
                    (currentKmLocations.last().timestamp - currentKmLocations.first().timestamp) / MS_TO_S
                currentKmTime.postValue((elapsedTime + remainingTime).toLong())
            }else{
                currentKmTime.postValue(LocationValues.NO_TIME)
            }
        }
    }


    fun stopLocationComputation() {
        currentSessionData.clear()
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

}