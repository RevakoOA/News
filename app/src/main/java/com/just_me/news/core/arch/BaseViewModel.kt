package com.just_me.news.core.arch

import android.arch.lifecycle.ViewModel

abstract class BaseViewModel<V: BaseContract.View, P: BaseContract.Presenter<V>>: ViewModel() {

    var presenter: P? = null
        set(p) {if (presenter == null) field = p}

    override fun onCleared() {
        super.onCleared()
        presenter?.onPresenterDestroy()
        presenter = null
    }
}