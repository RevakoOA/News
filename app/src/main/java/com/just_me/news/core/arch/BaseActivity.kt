package com.just_me.news.core.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity<V: BaseContract.View, P: BaseContract.Presenter<V>>: AppCompatActivity(), BaseContract.View {

    private val lifecycleRegistry = LifecycleRegistry(this)
    protected var presenter: P? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: BaseViewModel<V, P> = ViewModelProviders.of(this).get(BaseViewModel::class.java) as BaseViewModel<V, P>
        viewModel.presenter = initPresenter()
        presenter = viewModel.presenter
        presenter?.attachLifecycle(lifecycle)
        presenter?.attachView(this as V)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachLifecycle(lifecycle)
        presenter?.detachView()
    }
}