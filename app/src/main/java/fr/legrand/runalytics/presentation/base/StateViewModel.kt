package fr.legrand.runalytics.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class StateViewModel<T> : ViewModel() {

    protected abstract val currentViewState: T
    val viewState = MutableLiveData<T>()

    private val disposable = CompositeDisposable()

    protected inline fun <reified T> MutableLiveData<T>.update(block: T.() -> Unit) {
        this.postValue((currentViewState as T).apply(block))
    }

    override fun onCleared() {
        disposable.clear()
    }

}
