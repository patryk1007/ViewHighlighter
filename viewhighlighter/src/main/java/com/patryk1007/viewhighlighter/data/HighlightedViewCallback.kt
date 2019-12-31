package com.patryk1007.viewhighlighter.data

import android.graphics.Point

interface HighlightedViewCallback {
    fun onViewReady(viewData: HighlightedViewWithLabels, viewPosition: Point)
}