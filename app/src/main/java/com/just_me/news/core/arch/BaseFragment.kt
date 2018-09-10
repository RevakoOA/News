package com.just_me.news.core.arch

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.View

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>, VM: BaseViewModel<V, P>> : Fragment(), LifecycleRegistryOwner, BaseContract.View {

    private val lifecycleRegistry = LifecycleRegistry(this)
    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    inline fun <reified VM: BaseViewModel<V, P>> createViewModel(): VM {
        val viewModel = ViewModelProviders.of(this)[VM::class.java]
        viewModel.presenter = initPresenter()
        presenter = viewModel.presenter as P
        presenter?.attachLifecycle(lifecycle)
        presenter?.attachView(this as V)
        return viewModel
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachLifecycle(lifecycle)
        presenter?.detachView()
    }

    protected abstract fun initPresenter(): P
}
