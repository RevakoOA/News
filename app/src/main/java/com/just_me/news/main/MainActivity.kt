package com.just_me.news

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.CheckedTextView
import android.widget.TextView
import android.widget.Toast
import com.just_me.news.core.arch.BaseActivity
import com.just_me.news.news.MainPagerAdapter
import com.just_me.news.myNews.MyNewsFragment
import com.just_me.news.myNews.MyNewsFragment.Companion.IS_SELECTOR_VISIBLE
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_list_viewpager.*
import kotlinx.android.synthetic.main.search_layout.view.*
import kotlinx.android.synthetic.main.toolbar_content.*


class MainActivity :
        BaseActivity<MainActivityContract.View, MainActivityContract.Presenter>(),
        MainActivityContract.View {

    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var searchLayout: View
    lateinit var viewModel: MainViewModel
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun initPresenter(): MainActivityContract.Presenter = MainActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        setContentView(R.layout.activity_main)
        setViewPager()
        setSearch()
        ivMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        ivOptions.setOnClickListener {}
    }

    private fun setSearch() {
        searchLayout = navView.getHeaderView(0)
        searchLayout.apply {
            val updateEditTextHint = fun() {
                if (searchLayout.etSearch.text.isBlank()) {
                    var searchString: String = getString(R.string.search)
                    var list = ArrayList<String>(4)
                    if (searchLayout.tvcTopStories.isChecked) {
                        list.add(searchLayout.tvcTopStories.text.toString())
                    }
                    if (searchLayout.tvcMyNews.isChecked) {
                        list.add(searchLayout.tvcMyNews.text.toString())
                    }
                    if (searchLayout.tvcPopular.isChecked) {
                        list.add(searchLayout.tvcPopular.text.toString())
                    }
                    if (searchLayout.tvcVideo.isChecked) {
                        list.add(searchLayout.tvcVideo.text.toString())
                    }
                    if (list.size == 4) {
                        searchString += " " + getString(R.string.everywhere)
                        searchLayout.etSearch.hint = searchString
                        return
                    }
                    if (list.size == 0) {
                        searchString += " " + getString(R.string.everywhere)
                        searchLayout.etSearch.hint = searchString
                        return
                    }
                    searchString += list.joinToString(", ", " ${getString(R.string.prefix)} ")
                    searchLayout.etSearch.hint = searchString
                    return
                }
            }

            val search = fun(s: String, where: ArrayList<String>) {
                this@MainActivity.drawerLayout.closeDrawer(GravityCompat.START)
                pagerAdapter.items.forEach { if (where.contains(it.first)) (it.second as MyNewsFragment).filterItems(s) }
                val tabsCount = this@MainActivity.tabLayout.tabCount
                val tabs = ArrayList<View>(4)
                for (i in 0 until tabsCount) {
                    tabs.add((this@MainActivity.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i))
                }
                tabs.filter { !where.contains(((it as ViewGroup).getChildAt(1) as TextView).text) }
                        .forEach { it.isClickable= false; it.alpha = 0.3F; }
                val availableTabs = tabs.filter {  where.contains(((it as ViewGroup).getChildAt(1) as TextView).text) }
                availableTabs.forEach { it.isClickable = true ; it.alpha = 1.0F; }
                availableTabs[0]
                this@MainActivity.viewPager.currentItem = tabs.indexOf(availableTabs[0])
                lockViewPager(where.size != tabsCount)
            }

            val clean = fun() {
                this@MainActivity.drawerLayout.closeDrawer(GravityCompat.START)
                etSearch.setText(String())
                tvcTopStories.isChecked = false
                tvcMyNews.isChecked = false
                tvcPopular.isChecked = false
                tvcVideo.isChecked = false
                updateEditTextHint()
                val list = ArrayList<String>(4)
                resources.getStringArray(R.array.tabs).toCollection(list)
                search("", list)
                lockViewPager(false)
            }

            val tvcList = ArrayList<CheckedTextView>(4)
            tvcList.add(tvcTopStories)
            tvcList.add(tvcMyNews)
            tvcList.add(tvcPopular)
            tvcList.add(tvcVideo)
            for (tvc in tvcList) {
                tvc.setOnClickListener { (it as CheckedTextView).isChecked = !it.isChecked; updateEditTextHint() }
            }
            ivClose.setOnClickListener { clean() }
            etSearch.setOnEditorActionListener { tv: TextView, actionId: Int, _: KeyEvent? ->
                if (actionId == IME_ACTION_DONE) {
                    val selected = ArrayList<String>(4)
                    for (tvc in tvcList) {
                        if (tvc.isChecked) {
                            selected.add(tvc.text.toString())
                        }
                    }
                    if (selected.isEmpty()) {
                        selected.addAll(tvcList.map { it.text.toString() })
                    }
                    search(tv.text.toString(), selected)
                }
                false
            }
        }
    }

    private fun setViewPager() {
        // set tabs
        val names = arrayListOf<String>()
        names.addAll(resources.getStringArray(R.array.tabs))
        for (name in names) {
            tabLayout.addTab(tabLayout.newTab().setText(name))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply { viewPager.currentItem = position }
            }
        })

        // set adapter
        val bundle = Bundle()
        bundle.putBoolean(IS_SELECTOR_VISIBLE, false)
        val topStories = MyNewsFragment()
        topStories.arguments = bundle
        val popular = MyNewsFragment()
        popular.arguments = bundle
        val video = MyNewsFragment()
        video.arguments = bundle
        val fragments = arrayListOf<Fragment>(topStories, MyNewsFragment(), popular, video)
        val items = names.zip(fragments)

        // set ViewPager
        pagerAdapter = MainPagerAdapter(items, supportFragmentManager)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun lockViewPager(lock: Boolean) {
        viewPager.isPagingEnabled = !lock
        if (lock) {
            Toast.makeText(getActivity(), getString(R.string.swipe_not_allowed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getActivity() = this
}
