package com.just_me.news.myNews

import android.arch.lifecycle.MutableLiveData
import com.just_me.news.core.arch.BaseViewModel

class MyNewsFragmentViewModel: BaseViewModel<MyNewsContract.View, MyNewsContract.Presenter> () {

    var isSelectorVisible: MutableLiveData<Boolean> = MutableLiveData()
    var listOfRecyclerData: MutableLiveData<ArrayList<RecyclerData>> = MutableLiveData()

    init {
        isSelectorVisible.value = true
    }

}
