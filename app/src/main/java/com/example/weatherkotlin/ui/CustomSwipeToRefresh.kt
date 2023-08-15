package com.example.weatherkotlin.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class CustomSwipeToRefresh(context: Context?, attrs: AttributeSet?) :
    SwipeRefreshLayout(context!!, attrs) {
    private val mTouchSlop: Int
    private var mPrevX = 0f

    init {
        mTouchSlop = ViewConfiguration.get(context!!).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = event.x
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                val xDiff = Math.abs(eventX - mPrevX)
                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}
