package com.dv.comfortly.ui.ext

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description

fun LineChart.configureForApp(label: String? = null) {
    label?.let {
        description = Description().apply { text = label }
    }
    xAxis.setDrawLabels(false)
    setBackgroundColor(Color.WHITE)
    zoom(5f, 1f, 0f, 0f)
    isAutoScaleMinMaxEnabled = true
}
