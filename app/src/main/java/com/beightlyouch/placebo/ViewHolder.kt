package com.beightlyouch.placebo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.one_item.view.*

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var dateView: TextView? = null
    var titleView: TextView? = null

    init {
        dateView = itemView.dateView
        titleView = itemView.titleView
    }
}