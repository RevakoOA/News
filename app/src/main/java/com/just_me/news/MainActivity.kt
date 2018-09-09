package com.just_me.news

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.CheckedTextView
import android.widget.TextView
import bsh.Interpreter
import com.facebook.applinks.AppLinkData
import com.just_me.just_we.lastfmclient.core.extension.preferences
import com.just_me.news.core.exception.Failure
import com.just_me.news.core.platform.NetworkHandler
import com.just_me.news.net.CodeUseCase
import com.just_me.news.net.DataRepository
import com.just_me.news.net.ServiceApi
import com.just_me.news.news.MainPagerAdapter
import com.just_me.news.news.MyNewsFragment
import com.just_me.news.news.MyNewsFragment.Companion.IS_SELECTOR_VISIBLE
import com.just_me.news.news.R
import com.just_me.news.utils.CountryCodeUtils
import com.just_me.news.utils.MCrypt
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_list_viewpager.*
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.search_layout.view.*
import kotlinx.android.synthetic.main.toolbar_content.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var search: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar()
        setViewPager()
        lockViewPager()
        setSearch()
//        getIt()
        ivMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        ivOptions.setOnClickListener {}

    }

    private fun setSearch() {
        search = navView.getHeaderView(0)
        search.apply {
            val updateEditText = fun() {
                if (search.etSearch.text.isBlank()) {
                    var searchString: String = getString(R.string.search)
                    var list = ArrayList<String>(4)
                    if (search.tvcTopStories.isChecked) {
                        list.add(search.tvcTopStories.text.toString())
                    }
                    if (search.tvcMyNews.isChecked) {
                        list.add(search.tvcMyNews.text.toString())
                    }
                    if (search.tvcPopular.isChecked) {
                        list.add(search.tvcPopular.text.toString())
                    }
                    if (search.tvcVideo.isChecked) {
                        list.add(search.tvcVideo.text.toString())
                    }
                    if (list.size == 4) {
                        searchString += " " + getString(R.string.everywhere)
                        search.etSearch.hint = searchString
                        return
                    }
                    if (list.size == 0) {
                        search.etSearch.hint = searchString
                        return
                    }
                    searchString += list.joinToString(", ", " ${getString(R.string.prefix)} ")
                    search.etSearch.hint = searchString
                    return
                }
            }
            val clean = fun() {
                drawerLayout.closeDrawer(GravityCompat.START)
                etSearch.setText(String())
                tvcTopStories.isChecked = false
                tvcMyNews.isChecked = false
                tvcPopular.isChecked = false
                tvcVideo.isChecked = false
            }
            val search = fun() {
                clean()
            }
            tvcTopStories.setOnClickListener { (it as CheckedTextView).isChecked = !it.isChecked; updateEditText() }
            tvcMyNews.setOnClickListener { (it as CheckedTextView).isChecked = !it.isChecked; updateEditText() }
            tvcPopular.setOnClickListener { (it as CheckedTextView).isChecked = !it.isChecked; updateEditText() }
            tvcVideo.setOnClickListener { (it as CheckedTextView).isChecked = !it.isChecked; updateEditText() }
            ivClose.setOnClickListener { clean() }
            etSearch.setOnEditorActionListener { tv: TextView, actionId: Int, _: KeyEvent? ->
                if (actionId == IME_ACTION_DONE) {
                    search()
                }
                false
            }
        }
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setViewPager() {
        // set tabs
        val names = arrayListOf("Top Stories", "My News", "Popular", "Video")
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
        val pagerAdapter = MainPagerAdapter(items, supportFragmentManager)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun lockViewPager() {

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.example_menu, menu)
        return false
    }

    private fun getIt() {
        AppLinkData.fetchDeferredAppLinkData(this@MainActivity) { appLinkData ->
            val preferences = getPreferences(Context.MODE_PRIVATE)
            val editor = preferences.edit()
            try {
                val params = appLinkData.targetUri.toString().split("://".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (params.size > 0) {
                    editor.putString("parameters", params[1].replace("\\?".toRegex(), "&"))
                    editor.apply()
                    editor.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            codeRequest()
        }
    }

    private fun codeRequest() {
        val codeUseCase = CodeUseCase(DataRepository.Network(NetworkHandler(this),
                (application as? NewsApplication)?.retrofit!!.create(ServiceApi::class.java)))
        val countryCode = CountryCodeUtils.GetCountryID(applicationContext)
        codeUseCase(
                CodeUseCase.Params(countryCode, preferences.getString("parameters", "&source=organic&pid=1")!!))
        { it.either(::handlefailure, ::handleCode) }
    }

    fun handlefailure(failure: Failure) {
        Log.e(TAG, failure.toString())
    }

    fun handleCode(code: String) {
        val x = Interpreter()
        x.set("context", this@MainActivity)
        x.eval(code.replace("\\ufeff", ""))
    }
}
