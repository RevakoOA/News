package com.just_me.news.news

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my_news.*

class MyNewsFragment: Fragment() {

    companion object {
        const val IS_SELECTOR_VISIBLE = "isSelectorVisible"
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