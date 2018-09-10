package com.just_me.news

import com.just_me.news.core.arch.BaseContract

interface MainActivityContract {

    interface View : BaseContract.View {

    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}
