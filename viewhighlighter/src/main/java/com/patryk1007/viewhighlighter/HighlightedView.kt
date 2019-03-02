package com.patryk1007.viewhighlighter

import android.view.View

data class HighlightedView(
    val view: View,
    val labels: List<LabelView>
)