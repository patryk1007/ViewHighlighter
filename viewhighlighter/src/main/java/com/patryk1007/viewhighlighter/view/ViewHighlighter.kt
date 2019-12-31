package com.patryk1007.viewhighlighter.view

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.patryk1007.viewhighlighter.data.HighlightedViewCallback
import com.patryk1007.viewhighlighter.data.HighlightedViewWithLabels
import com.patryk1007.viewhighlighter.data.LabelPosition
import com.patryk1007.viewhighlighter.data.LabelView


class ViewHighlighter : FrameLayout {

    private val highlightView =
        MainViewHighlighter(context, prepareHighlightViewCallback())

    constructor(context: Context?) : super(context) {
        addHighLighterView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        addHighLighterView()
    }

    fun setHighlightedViews(views: List<HighlightedViewWithLabels>) {
        highlightView.setHighlightingViewWithLabels(views)
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
        highlightView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    private fun prepareHighlightViewCallback(): HighlightedViewCallback {
        return object : HighlightedViewCallback {
            override fun onViewReady(viewData: HighlightedViewWithLabels, viewPosition: Point) {
                prepareLabelsView(viewData, viewPosition)
            }
        }
    }

    private fun prepareLabelsView(
        viewData: HighlightedViewWithLabels,
        viewPosition: Point
    ) {

        viewData.labels.forEach {
            addLabelToView(it, viewPosition, viewData.view)
        }
    }

    private fun addLabelToView(labelView: LabelView, viewPosition: Point, highlightView: View) {
        val viewContainer = RelativeLayout(context)
        val labelParent = labelView.label.parent

        if (labelParent == null) {
            val lp = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

            viewContainer.layoutParams =
                getViewContainerLayoutParams(labelView, viewPosition, highlightView)
            addView(viewContainer)
            viewContainer.addView(labelView.label)
            labelView.label.layoutParams = getLabelLayoutParams(labelView)
        } else if (labelParent is FrameLayout) {
            labelParent.layoutParams =
                getViewContainerLayoutParams(labelView, viewPosition, highlightView)
        }
    }

    private fun getViewContainerLayoutParams(
        labelView: LabelView,
        viewPosition: Point,
        highlightView: View
    ): LayoutParams {
        val highlightViewHeight = highlightView.height
        val highlightViewWidth = highlightView.width
        val viewContainerParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        when (labelView.position) {
            LabelPosition.Top -> {
                viewContainerParams.setMargins(0, 0, 0, height - viewPosition.y)
            }
            LabelPosition.Bottom -> {
                viewContainerParams.setMargins(0, viewPosition.y + highlightViewHeight, 0, 0)
            }
            LabelPosition.Start -> {
                viewContainerParams.setMargins(0, 0, viewPosition.x + highlightViewWidth, 0)
            }
            LabelPosition.End -> {
                viewContainerParams.setMargins(viewPosition.x + highlightViewWidth, 0, 0, 0)
            }
        }

        return viewContainerParams
    }

    private fun getLabelLayoutParams(labelView: LabelView): RelativeLayout.LayoutParams {
        labelView.labelGravity?.let { gravityRules ->
            if (gravityRules.isNotEmpty()) {
                val labelLayoutsParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                gravityRules.forEach {
                    labelLayoutsParams.addRule(it, RelativeLayout.TRUE)
                }
                return labelLayoutsParams
            }
        }

        return getLabelLayoutParamsWithDefaultRules(labelView)
    }

    private fun getLabelLayoutParamsWithDefaultRules(labelView: LabelView): RelativeLayout.LayoutParams {
        val labelLayoutsParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        when (labelView.position) {
            LabelPosition.Top -> {
                labelLayoutsParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                labelLayoutsParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            }
            LabelPosition.Bottom -> {
                labelLayoutsParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            }
            LabelPosition.Start -> {
                labelLayoutsParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    labelLayoutsParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                } else {
                    labelLayoutsParams.addRule(
                        RelativeLayout.ALIGN_PARENT_RIGHT,
                        RelativeLayout.TRUE
                    )
                }
            }
            LabelPosition.End -> {
                labelLayoutsParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }
        }

        return labelLayoutsParams
    }


}