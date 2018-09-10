package com.just_me.news.myNews

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.just_me.news.core.arch.BasePresenter
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.platform.NetworkHandler
import com.just_me.news.net.DataRecyclerUseCase
import com.just_me.news.net.DataRepository
import com.just_me.news.net.ServiceApi
import com.just_me.news.net.UseCase

class MyNewsPresenter: BasePresenter<MyNewsContract.View>(), MyNewsContract.Presenter {

    lateinit var dataUseCase: DataRecyclerUseCase
    lateinit var serviceApi: ServiceApi

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate in MyNewsPresenter")
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    fun onStart() {
        serviceApi = view!!.getApplication().retrofit.create(ServiceApi::class.java)
        val network = DataRepository.Network(NetworkHandler(view!!.getApplication()), serviceApi)
        dataUseCase = DataRecyclerUseCase(network)
        dataUseCase(UseCase.None()) {it.either(::handleFailure,::handleData)}
    }

    protected fun handleFailure(failure: Failure) {
        Log.e(TAG, failure.toString())
    }

    private fun handleData(data: ArrayList<RecyclerData>) {
        view?.viewModel()?.listOfRecyclerData?.value = data
    }
}