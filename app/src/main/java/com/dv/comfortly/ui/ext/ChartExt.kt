package com.dv.comfortly.ui.ext

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description

fun LineChart.configureForApp(label: String? = null) {
    label?.let {
        description = Description().apply { text = label }
    }
    xAxis.setDrawLabels(false)
    xAxis.granularity = 1.0f
    setBackgroundColor(Color.WHITE)
    zoom(2f, 1f, 0f, 0f)
    isAutoScaleMinMaxEnabled = true
}
