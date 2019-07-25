package fr.legrand.runalytics.presentation.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.ui.map.item.RALocationViewDataWrapper
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MapFragmentViewModel(private val locationRepository: LocationRepository) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val traveledDistanceLiveData = MutableLiveData<RALocationViewDataWrapper>()


    init {
        startLocationComputation()
    }

    override fun onCleared() {
        disposable.clear()
    }

    private fun startLocationComputation() {
        locationRepository.startLocationComputation().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onNext = {
                traveledDistanceLiveData.postValue(RALocationViewDataWrapper(it))
            }
        ).addToComposite(disposable)
    }
}