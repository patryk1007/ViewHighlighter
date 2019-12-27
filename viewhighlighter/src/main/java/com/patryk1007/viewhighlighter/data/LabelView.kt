package com.patryk1007.viewhighlighter.data

import android.view.View

data class LabelView(
    val label: View,
    val position: LabelPosition,
    val labelGravity: List<Int>? = null
)