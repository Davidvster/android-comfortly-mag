package com.dv.comfortly.ui.base.utils

import android.view.View

/**
 * This is a decorator for View click listeners which will prevent multiple clicks
 * being triggered in a short amount of time.
 */
abstract class ThrottlingClickListener(
    private val onClickListener: (v: View) -> Unit,
) : View.OnClickListener {
    abstract var lastClickMillis: Long

    companion object {
        /**
         * The cooldown during which click events are discarded. This is global and shared for all decorated listeners.
         */
        private const val CLICK_COOLDOWN_MS: Long = 500
    }

    /**
     * @return true if the event should be forwarded to the click listener this class is decorating.
     */
    private fun shouldProxyEvent(): Boolean {
        return System.currentTimeMillis() - lastClickMillis > CLICK_COOLDOWN_MS
    }

    override fun onClick(v: View) {
        if (shouldProxyEvent()) {
            lastClickMillis = System.currentTimeMillis()
            onClickListener(v)
        }
    }
}

/**
 * Disables multiple clicks only for current view
 */
class LocalThrottleClickListener(
    onClickListener: (v: View) -> Unit,
) : ThrottlingClickListener(onClickListener) {
    private var localLastClickMillis: Long = 0

    override var lastClickMillis: Long
        get() = localLastClickMillis
        set(value) {
            localLastClickMillis = value
        }
}

/**
 * Disables multiple clicks for all views that use the global throttle class
 */
class GlobalThrottleClickListener(
    onClickListener: (v: View) -> Unit,
) : ThrottlingClickListener(onClickListener) {
    companion object {
        private var globalLastClickMillis: Long = 0
    }

    override var lastClickMillis: Long
        get() = globalLastClickMillis
        set(value) {
            globalLastClickMillis = value
        }
}
