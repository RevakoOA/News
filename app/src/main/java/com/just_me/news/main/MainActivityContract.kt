package com.just_me.news

import com.just_me.news.core.arch.BaseContract

interface MainActivityContract {

    interface View : BaseContract.View {
        fun getActivity(): MainActivity
    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}
