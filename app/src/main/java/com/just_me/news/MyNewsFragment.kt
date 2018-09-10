package com.just_me.news.news

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
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

    lateinit var dataUseCase: DataRecyclerUseCase
    lateinit var serviceApi: ServiceApi
    lateinit var datas : ArrayList<RecyclerData>
    lateinit var adapter: MyRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceApi = (activity?.applicationContext as NewsApplication).retrofit.create(ServiceApi::class.java)
        val network = DataRepository.Network(NetworkHandler(context!!), serviceApi)
        dataUseCase = DataRecyclerUseCase(network)
    }

    protected fun handleFailure(failure: Failure) {
        Log.e(TAG, failure.toString())
    }

    private fun handleData(data: ArrayList<RecyclerData>) {
        datas = data
        if (!::adapter.isInitialized) {
            adapter = MyRecyclerAdapter(datas)
        } else {
            adapter
        }
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
        tlSortSelector.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            var currentToast: Toast? = null
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                currentToast?.cancel()
                currentToast = Toast.makeText(activity, p0?.text, Toast.LENGTH_SHORT)
                currentToast?.show()
            }
        })
        dataUseCase(UseCase.None()) {it.either(::handleFailure,::handleData)}
    }

    private fun hideSelector(hide: Boolean) {
        tlSortSelector.visibility = if (hide) GONE else VISIBLE
    }

    /**
     * Show only if {@param s} contains in data item
     */
    fun filterItems(s: String) {
        adapter.filterDatas(s)
    }
}