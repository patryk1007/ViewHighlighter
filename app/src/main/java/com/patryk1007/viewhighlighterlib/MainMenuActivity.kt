package com.patryk1007.viewhighlighterlib

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        initButtons()
    }

    private fun initButtons(){
        menuAlphaViewButton.setOnClickListener {
            startActivity(Intent(this, AlphaExampleActivity::class.java))
        }
        menuListButton.setOnClickListener {
            startActivity(Intent(this, ListExampleActivity::class.java))
        }
        menuTextButton.setOnClickListener {
            startActivity(Intent(this, TextExampleActivity::class.java))
        }
    }
}
