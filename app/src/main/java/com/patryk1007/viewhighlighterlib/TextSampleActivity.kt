package com.patryk1007.viewhighlighterlib

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CompoundButton
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_text_sample.*

class TextSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_sample)
        viewHighlighter.highlightedViews.addAll(arrayListOf(testTextA))
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
        fillTransparentCheckBox.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                updateTransparentBackgroundMode()
            }
        })
        updateTransparentBackgroundMode()
    }

    private fun updateTransparentBackgroundMode(){
        viewHighlighter.fillTransparentPixels = fillTransparentCheckBox.isChecked
        viewHighlighter.notifyViewSetChanged()
    }
}
