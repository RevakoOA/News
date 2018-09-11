package com.just_me.news.myNews

import com.just_me.news.core.arch.BaseContract
import com.just_me.news.core.arch.BaseViewModel

interface MyNewsContract {

    interface View: BaseContract.View {
        fun showSelector(show: Boolean)
        fun viewModel(): MyNewsFragmentViewModel
    }

    interface Presenter: BaseContract.Presenter<View> {

    }

}