package fr.legrand.runalytics.presentation.ui.session.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.daifen.application.presentation.base.SingleLiveEvent
import fr.legrand.runalytics.data.repository.LocationRepository
import fr.legrand.runalytics.presentation.ui.session.list.item.SessionViewDataWrapper
import fr.legrand.runalytics.presentation.utils.addToComposite
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SessionListFragmentViewModel(private val locationRepository: LocationRepository) :
    ViewModel() {

    private val disposable = CompositeDisposable()
    val errorEvent = SingleLiveEvent<Throwable>()
    val sessionList = MutableLiveData<List<SessionViewDataWrapper>>()

    init {
        getAllSessions()
    }

    override fun onCleared() {
        disposable.clear()
    }

    fun getAllSessions() {
        locationRepository.getAllSessions().subscribeOn(Schedulers.io()).subscribeBy(
            onError = {
                errorEvent.postValue(it)
            },
            onSuccess = {
                sessionList.postValue(it.map { SessionViewDataWrapper(it) })
            }
        ).addToComposite(disposable)
    }
}