package com.xiaolu.applicationmanage.util

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


object AnimationUtil {
    @JvmStatic
    fun fadeIn(view: View, startAlpha: Float, endAlpha: Float, duration: Long) {
        if (view.visibility == View.VISIBLE) return
        view.visibility = View.VISIBLE
        val animation: Animation = AlphaAnimation(startAlpha, endAlpha)
        animation.duration = duration
        view.startAnimation(animation)
    }

    @JvmStatic
    fun fadeIn(view: View) {
        fadeIn(view, 0f, 1f, 400)

        // We disabled the button in fadeOut(), so enable it here.
        view.isEnabled = true
    }

    @JvmStatic
    fun fadeOut(view: View) {
        if (view.visibility != View.VISIBLE) return

        // Since the button is still clickable before fade-out animation
        // ends, we disable the button first to block click.
        view.isEnabled = false
        val animation: Animation = AlphaAnimation(1f, 0f)
        animation.duration = 400
        view.startAnimation(animation)
        view.visibility = View.GONE
    }
}