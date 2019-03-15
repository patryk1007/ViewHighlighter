package com.patryk1007.viewhighlighter.data

import android.graphics.Point

interface HighlightViewCallback {
    fun onViewReady(viewData: HighlightedViewWithLabels, viewPosition: Point)
}