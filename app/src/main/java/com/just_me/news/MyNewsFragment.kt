package com.just_me.news.news

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.just_me.news.MyRecyclerAdapter
import com.just_me.news.NewsApplication
import com.just_me.news.RecyclerData
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.platform.NetworkHandler
import com.just_me.news.net.*
import kotlinx.android.synthetic.main.fragment_my_news.*

class MyNewsFragment: Fragment() {

    companion object {
        const val TAG = "MyNewsFragment"
        const val IS_SELECTOR_VISIBLE = "isSelectorVisible"
    }

    lateinit var serviceApi: ServiceApi
    lateinit var datas: List<RecyclerData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceApi = (activity?.applicationContext as NewsApplication).retrofit.create(ServiceApi::class.java)
        val network = DataRepository.Network(NetworkHandler(context!!), serviceApi)
        val dataUseCase = DataRecyclerUseCase(network)
        dataUseCase(UseCase.None()) {it.either(::handleFailure,::handleData)}
    }

    protected fun handleFailure(failure: Failure) {
        Log.e(TAG, failure.toString())
    }

    private fun handleData(data: List<RecyclerData>) {
        Log.d(TAG, data.toString())
        datas = data
        val adapter = MyRecyclerAdapter(datas)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectorVisibility = arguments?.getBoolean(IS_SELECTOR_VISIBLE, true)?:true
        hideSelector(!selectorVisibility)
    }

    private fun hideSelector(hide: Boolean) {
        selector.visibility = if (hide) GONE else VISIBLE
    }
}