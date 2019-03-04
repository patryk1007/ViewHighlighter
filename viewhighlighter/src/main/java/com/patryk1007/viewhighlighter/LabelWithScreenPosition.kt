package com.patryk1007.viewhighlighter

import android.graphics.Point
import android.view.View

data class LabelWithScreenPosition(
    val label: View,
    val position: Point
)