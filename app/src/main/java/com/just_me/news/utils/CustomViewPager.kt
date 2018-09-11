package com.just_me.news.utils

import android.content.Context
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.support.v4.view.ViewPager
import android.util.AttributeSet


class CustomViewPager : ViewPager {

    var isPagingEnabled = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event)
    }
}