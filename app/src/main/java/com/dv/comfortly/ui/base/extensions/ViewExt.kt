package com.dv.comfortly.ui.base.extensions

import android.view.View
import com.dv.comfortly.ui.base.utils.GlobalThrottleClickListener
import com.dv.comfortly.ui.base.utils.LocalThrottleClickListener

/**
 * Global = false -> Disables clicks only for current view
 * Global = true -> Disables clicks for all views that use the global throttle extension
 */
fun View.setThrottleClickListener(
    global: Boolean = false,
    listener: (v: View) -> Unit,
) {
    when {
        global -> this.setOnClickListener(GlobalThrottleClickListener(listener))
        !global -> this.setOnClickListener(LocalThrottleClickListener(listener))
    }
}
