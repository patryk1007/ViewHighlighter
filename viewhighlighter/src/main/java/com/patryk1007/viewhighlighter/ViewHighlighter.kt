package com.patryk1007.viewhighlighter

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.patryk1007.viewhighlighter.ext.waitForLayout


class ViewHighlighter : FrameLayout {

    private val highlightView = HighlightView(context, prepareHighlightViewCallback())

    constructor(context: Context?) : super(context) {
        addHighLighterView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        addHighLighterView()
    }

    fun setHighlightingViewWithLabels(views: List<HighlightedViewWithLabels>) {
        highlightView.setHighlightingViewWithLabels(views)
        notifyViewSetChanged()
    }

    fun setHighlightingView(views: List<View>) {
        val newHighLighted = arrayListOf<HighlightedViewWithLabels>()
        views.forEach {
            newHighLighted.add(HighlightedViewWithLabels(it, arrayListOf()))
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

    private fun prepareHighlightViewCallback(): HighlightViewCallback {
        return object : HighlightViewCallback {
            override fun onViewReady(viewData: HighlightedViewWithLabels, viewPosition: Point) {
                prepareLabelsView(viewData, viewPosition)
            }
        }
    }

    private fun prepareLabelsView(
        viewData: HighlightedViewWithLabels,
        viewPosition: Point
    ) {
        val params =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        viewData.labels.forEach {
            val label = it.label
            label.layoutParams = params
            addView(label)

            label.waitForLayout {
                setLabelPadding(it, viewPosition, viewData.view)
            }
        }
    }

    private fun setLabelPadding(labelView: LabelView, viewPosition: Point, highlightView: View) {
        val padding = when (labelView.position) {
            LabelPosition.Top -> {
                Point(0, viewPosition.y - labelView.label.height)
            }
            LabelPosition.Bottom -> {
                Point(0, viewPosition.y + highlightView.height)
            }
            LabelPosition.Start -> {
                Point(viewPosition.x - labelView.label.width, 0)
            }
            LabelPosition.End -> {
                Point(viewPosition.x + highlightView.height, 0)
            }
        }
        labelView.label.setPadding(padding.x, padding.y, 0, 0)
    }

}