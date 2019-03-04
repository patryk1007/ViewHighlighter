package com.patryk1007.viewhighlighter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout


class ViewHighlighter : FrameLayout {

    private val highlightView = HighlightView(context)

    constructor(context: Context?) : super(context) {
        addHighLighterView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        addHighLighterView()
    }

    fun setHighlightingViewWithLabels(views: List<HighlightedView>) {
        highlightView.setHighlightingViewWithLabels(views)
        notifyViewSetChanged()
    }

    fun setHighlightingView(views: List<View>) {
        val newHighLighted = arrayListOf<HighlightedView>()
        views.forEach {
            newHighLighted.add(HighlightedView(it, arrayListOf()))
        }
        highlightView.setHighlightingViewWithLabels(newHighLighted)
        notifyViewSetChanged()
    }

    /**
     * ViewHighlighter background color
     */
    fun setColor(color: Int) {
        highlightView.setColor(color)
    }

    fun setFillTransparentPixels(enable: Boolean) {
        highlightView.fillTransparentPixels = enable
    }

    /**
     * Sets view alpha fill level to a value from 0 to 1, where 0 means the only completely transparent color is filled  and 1 means even solid color is filled.
     * Default value is 0.5
     */
    fun setAlphaLevel(alpha: Float) {
        highlightView.setAlphaLevel(alpha)
    }

    /**
     * Notifies the view that highlighted views have changed and the view should refresh itself
     */
    fun notifyViewSetChanged() {
        highlightView.notifyViewSetChanged()
        invalidate()
    }

    private fun addHighLighterView() {
        addView(highlightView)
        highlightView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

}