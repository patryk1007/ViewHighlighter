package com.patryk1007.viewhighlighterlib

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patryk1007.viewhighlighter.FullScreenHighlight
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.view_dialog_custom.view.*


class MainActivity : AppCompatActivity() {

    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initList()
        initButton()
    }

    private fun initList() {
        simpleRecyclerView.layoutManager = layoutManager
        simpleRecyclerView.adapter = Adapter()
    }

    private fun initButton() {
        highlightItemButton.setOnClickListener {
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.view_dialog_custom, null)
            val listItemView = layoutManager.findViewByPosition(4)
            val highlightedViews = arrayListOf<View>()
            listItemView?.let(highlightedViews::add)

            val highlighter = FullScreenHighlight(this, highlightedViews, dialogView)
            highlighter.viewHighlighter.setColor(Color.parseColor("#000000"))
            highlighter.show()

            dialogView.closeViewButton.setOnClickListener {
                highlighter.dismiss()
            }
        }
    }


    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
            vh.text.text = "POSITION $p1"
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.tvAnimalName
    }
}
