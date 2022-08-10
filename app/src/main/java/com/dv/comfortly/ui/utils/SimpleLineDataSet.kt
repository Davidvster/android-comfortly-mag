package com.dv.comfortly.ui.utils

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class SimpleLineDataSet(
    entries: List<Entry> = emptyList(),
    dataLabel: String,
    lineColor: Int
) : LineDataSet(entries, dataLabel) {
    init {
        setDrawCircles(false)
        setDrawValues(false)
        color = lineColor
    }
}
