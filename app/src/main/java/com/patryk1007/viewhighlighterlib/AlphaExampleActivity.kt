package com.patryk1007.viewhighlighterlib

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.patryk1007.viewhighlighter.data.HighlightedViewWithLabels
import kotlinx.android.synthetic.main.activity_alpha_example.*
import kotlinx.android.synthetic.main.activity_text_sample.*
import kotlinx.android.synthetic.main.activity_text_sample.alphaLevelSlider
import kotlinx.android.synthetic.main.activity_text_sample.alphaLevelText
import kotlinx.android.synthetic.main.activity_text_sample.fillTransparentCheckBox
import kotlinx.android.synthetic.main.activity_text_sample.viewHighlighter

class AlphaExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpha_example)
        initView()
    }


    private fun initView() {
        initViewHighlighted()
        initSlider()
        initCheckbox()
    }

    private fun initViewHighlighted() {
        val highlightedViews = arrayListOf<HighlightedViewWithLabels>()
        highlightedViews.add(HighlightedViewWithLabels(someDrunkGuysImage))
        highlightedViews.add(HighlightedViewWithLabels(rectangleImage))
        highlightedViews.add(HighlightedViewWithLabels(circleImage))

        viewHighlighter.setHighlightedViews(highlightedViews)
    }

    private fun initSlider() {
        alphaLevelSlider.max = 100
        alphaLevelSlider.progress = 50
        alphaLevelSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                updateSliderProgress()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        updateSliderProgress()
    }

    private fun updateSliderProgress() {
        val newValue = alphaLevelSlider.progress / 100f
        viewHighlighter.setAlphaLevel(newValue)
        viewHighlighter.notifyViewSetChanged()
        alphaLevelText.text = String.format(getString(R.string.alpha_level), newValue.toString())
    }

    private fun initCheckbox() {
        fillTransparentCheckBox.setOnCheckedChangeListener { p0, p1 -> updateTransparentBackgroundMode() }
        updateTransparentBackgroundMode()
    }

    private fun updateTransparentBackgroundMode() {
        val transparentModeSelected = fillTransparentCheckBox.isChecked

        alphaLevelSlider.visibility = if(transparentModeSelected) View.VISIBLE else View.GONE
        alphaLevelText.visibility = if(transparentModeSelected) View.VISIBLE else View.GONE

        viewHighlighter.setFillTransparentPixels(transparentModeSelected)
        viewHighlighter.notifyViewSetChanged()
    }
}
