package com.just_me.news.core.arch

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import com.just_me.news.NewsApplication

interface BaseContract {

    interface View {
        fun application(): NewsApplication
    }

    interface Presenter<V : BaseContract.View> {

        var stateBundle: Bundle

        var view: V?

        fun isViewAttached(): Boolean

        fun attachLifecycle(lifecycle: Lifecycle)

        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachView(view: V)

        fun detachView()

        fun onPresenterDestroy()
    }
}