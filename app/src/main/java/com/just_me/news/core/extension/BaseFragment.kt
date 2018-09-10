package com.just_me.news.core.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.just_me.news.core.arch.BaseContract
import com.just_me.news.core.arch.BaseFragment
import com.just_me.news.core.arch.BaseViewModel

inline fun <reified T : ViewModel>
        BaseFragment<BaseContract.View, BaseContract.Presenter<BaseContract.View>,
                BaseViewModel<BaseContract.View, BaseContract.Presenter<BaseContract.View>>>
        .createViewModel(): T {
            val vm = ViewModelProviders.of(this)[T::class.java]
            return vm
        }