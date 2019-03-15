package com.patryk1007.viewhighlighter.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.patryk1007.viewhighlighter.data.HighlightedViewWithLabels
import com.patryk1007.viewhighlighter.R
import kotlinx.android.synthetic.main.highlight_view.*
import java.util.*

class FullScreenHighlight(
    context: Context,
    private val highlightedViews: ArrayList<HighlightedViewWithLabels>,
    private val layoutView: View? = null
) : Dialog(context) {

    val viewHighlighter: ViewHighlighter =
        ViewHighlighter(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.highlight_view)
        updateViewPosition()
        addViewHighlighter()
        addLayoutView()
    }

    private fun updateViewPosition() {
        window?.attributes?.let {
            val windowAttributes = it
            windowAttributes.width = ViewGroup.LayoutParams.MATCH_PARENT
            windowAttributes.height = ViewGroup.LayoutParams.MATCH_PARENT
            window?.setDimAmount(0f)
            window?.attributes = windowAttributes
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun addViewHighlighter() {
        viewHighlighter.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogRootView.addView(viewHighlighter)
        viewHighlighter.setHighlightingViewWithLabels(highlightedViews)
    }

    private fun addLayoutView() {
        layoutView?.let {
            dialogRootView.addView(it)
        }
    }

}