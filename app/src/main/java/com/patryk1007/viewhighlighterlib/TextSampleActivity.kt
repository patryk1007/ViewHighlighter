package com.patryk1007.viewhighlighterlib

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.patryk1007.viewhighlighter.data.HighlightedViewWithLabels
import com.patryk1007.viewhighlighter.data.LabelPosition
import com.patryk1007.viewhighlighter.data.LabelView
import kotlinx.android.synthetic.main.activity_text_sample.*

class TextSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_sample)


        val inflater = this.layoutInflater
        val labelView = inflater.inflate(R.layout.label_view, null)
        val labelView2 = inflater.inflate(R.layout.label_view, null)
        val labelView3 = inflater.inflate(R.layout.label_view, null)
        val labelView4 = inflater.inflate(R.layout.label_view, null)

        val highlightedViews = arrayListOf<HighlightedViewWithLabels>()
        highlightedViews.add(
            HighlightedViewWithLabels(
                testTextA,
                listOf(
                    LabelView(
                        labelView,
                        LabelPosition.Bottom
                    ),
                    LabelView(
                        labelView2,
                        LabelPosition.Top
                    )
                    ,
                    LabelView(
                        labelView3,
                        LabelPosition.Start
                    ),
                    LabelView(
                        labelView4,
                        LabelPosition.End
                    )
                )
            )
        )

        viewHighlighter.setHighlightingViewWithLabels(highlightedViews)
        initView()
    }

    private fun initView() {
        initSlider()
        initButton()
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

    private fun initButton() {
        testListButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        fillTransparentCheckBox.setOnCheckedChangeListener { p0, p1 -> updateTransparentBackgroundMode() }
        updateTransparentBackgroundMode()
    }

    private fun updateTransparentBackgroundMode() {
        viewHighlighter.setFillTransparentPixels(fillTransparentCheckBox.isChecked)
        viewHighlighter.notifyViewSetChanged()
    }
}
