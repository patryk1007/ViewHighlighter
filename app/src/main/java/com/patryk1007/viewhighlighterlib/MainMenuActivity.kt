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
        menuFullViewButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        menuListButton.setOnClickListener {
            //todo implement me
        }
        menuTextButton.setOnClickListener {
            startActivity(Intent(this, TextSampleActivity::class.java))
        }
    }
}
