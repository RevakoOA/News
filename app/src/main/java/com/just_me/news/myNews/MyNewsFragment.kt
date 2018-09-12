package com.just_me.news.myNews

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.just_me.news.NewsApplication
import com.just_me.news.core.arch.BaseFragment
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.fragment_my_news.*

class MyNewsFragment: BaseFragment<MyNewsContract.View, MyNewsContract.Presenter, MyNewsFragmentViewModel>(), MyNewsContract.View {

    companion object {
        const val TAG = "MyNewsFragment"
        const val IS_SELECTOR_VISIBLE = "isSelectorVisible"
    }

    val adapter = MyRecyclerAdapter()
    lateinit var viewModel: MyNewsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        viewModel.isSelectorVisible.observe(this, Observer {
            showSelector(it!!)
        })
        viewModel.listOfRecyclerData.observe(this, Observer {
            if (it != null) {
                adapter.items = it
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isSelectorVisible.value = arguments?.getBoolean(IS_SELECTOR_VISIBLE, true)?:true
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
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = adapter
    }

    /**
     * Show only if {@param s} contains in data item
     */
    fun filterItems(s: String) {
        adapter.filterDatas(s)
    }

    override fun initPresenter(): MyNewsContract.Presenter = MyNewsPresenter()

    override fun application(): NewsApplication = activity!!.applicationContext as NewsApplication

    override fun viewModel() = viewModel

    override fun showSelector(show: Boolean) {
        tlSortSelector.visibility = if (show) VISIBLE else GONE
    }
}