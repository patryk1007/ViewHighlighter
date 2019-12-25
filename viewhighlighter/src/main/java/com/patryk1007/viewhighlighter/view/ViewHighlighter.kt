package com.patryk1007.viewhighlighter.view

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.patryk1007.viewhighlighter.data.HighlightViewCallback
import com.patryk1007.viewhighlighter.data.HighlightedViewWithLabels
import com.patryk1007.viewhighlighter.data.LabelPosition
import com.patryk1007.viewhighlighter.data.LabelView


class ViewHighlighter : FrameLayout {

    private val highlightView =
        HighlightView(context, prepareHighlightViewCallback())

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
        highlightView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
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

        viewData.labels.forEach {
            addLabelToView(it, viewPosition, viewData.view)
        }
    }

    private fun addLabelToView(labelView: LabelView, viewPosition: Point, highlightView: View) {
        val highlightViewHeight = highlightView.height
        val highlightViewWidth = highlightView.width
        val viewContainer = RelativeLayout(context)
        val labelParent = labelView.label.parent

        val viewContainerParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        val labelParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

        when (labelView.position) {
            LabelPosition.Top -> {
                viewContainerParams.setMargins(0,0,0, height-viewPosition.y)
                labelParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            }
            LabelPosition.Bottom -> {
                viewContainerParams.setMargins(0,viewPosition.y + highlightViewHeight,0,0)
                labelParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            }
            LabelPosition.Start -> {
                viewContainerParams.setMargins(0,0,viewPosition.x + highlightViewWidth, 0)
                labelParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    labelParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                }else{
                    labelParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                }
            }
            LabelPosition.End -> {
                viewContainerParams.setMargins(viewPosition.x + highlightViewWidth,0,0, 0)
                labelParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }
        }



        if(labelParent == null) {
            val lp = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

            viewContainer.layoutParams = viewContainerParams
            viewContainer.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_green_light
                )
            )
            addView(viewContainer)
            viewContainer.addView(labelView.label)
            labelView.label.layoutParams = labelParams
        } else if (labelParent is FrameLayout){
            labelParent.layoutParams = viewContainerParams
        }
    }



}