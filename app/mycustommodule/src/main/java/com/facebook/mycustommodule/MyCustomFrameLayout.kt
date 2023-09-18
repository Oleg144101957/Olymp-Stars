package com.facebook.mycustommodule

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar

class MyCustomFrameLayout(private val context: Context): FrameLayout(context) {

    private val progressBar: ProgressBar
    init {
        setBackgroundColor(Color.BLACK)
        progressBar = ProgressBar(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        addView(progressBar)
    }

    fun showProgressBar() {
        progressBar.visibility = VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = GONE
    }
}