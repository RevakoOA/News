package com.just_me.news.myNews

import com.just_me.news.core.arch.BaseContract

interface MyNewsContract {

    interface View: BaseContract.View {

    }

    interface Presenter: BaseContract.Presenter<View> {

    }

}