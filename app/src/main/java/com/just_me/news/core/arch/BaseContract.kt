package com.just_me.news.core.arch

import android.arch.lifecycle.Lifecycle
import android.os.Bundle

interface BaseContract {

    interface View

    interface Presenter<V : BaseContract.View> {

        var stateBundle: Bundle

        var view: V?

        val isViewAttached: Boolean

        fun attachLifecycle(lifecycle: Lifecycle)

        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachView(view: V)

        fun detachView()

        fun onPresenterDestroy()
    }
}