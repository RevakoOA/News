package com.just_me.news.core.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.just_me.news.myNews.NewsApplication

abstract class BaseActivity<V: BaseContract.View, P: BaseContract.Presenter<V>>: AppCompatActivity(), BaseContract.View {

    private val lifecycleRegistry = LifecycleRegistry(this)
    protected var presenter: P? = null

    @CallSuper
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

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachLifecycle(lifecycle)
        presenter?.detachView()
    }

    override fun application(): NewsApplication = this.application as NewsApplication
}