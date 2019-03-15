package com.patryk1007.viewhighlighter.data

import android.view.View

data class HighlightedViewWithLabels(
    val view: View,
    val labels: List<LabelView>
)