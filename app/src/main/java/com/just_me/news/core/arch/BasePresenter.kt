package com.just_me.news.core.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import android.util.Log

abstract class BasePresenter<V: BaseContract.View>: LifecycleObserver, BaseContract.Presenter<V> {

    companion object {
        const val TAG = "BasePresenter"
    }

    override var stateBundle: Bundle = Bundle.EMPTY

    override var view: V? = null

    override val isViewAttached: Boolean = view != null

    override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    override fun attachView(view: V) { this.view = view }


    override fun detachView() { view = null }

    override fun onPresenterDestroy() {
        if (!stateBundle.isEmpty) {
            stateBundle.clear()
        }
    }

//    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
//    fun onCreate() {
//        Log.d(TAG, "onCreate called with Lifecycle.Event")
//    }
}