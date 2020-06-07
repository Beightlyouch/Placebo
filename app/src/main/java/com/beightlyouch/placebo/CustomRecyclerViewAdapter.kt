package com.beightlyouch.placebo

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults

class CustomRecyclerViewAdapter(realmResults: RealmResults<PlaceboButton>): RecyclerView.Adapter<ViewHolder>() {
    private val rResults: RealmResults<PlaceboButton> = realmResults
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Viewの生成 => ViewHolderの生成
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_item, parent, false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //ViewHolder <=BIND=> Data
        val pb = rResults[position]
        holder.dateView?.text = DateFormat.format("yyyy/MM/dd", pb?.dateTime)
        holder.titleView?.text = pb?.title
    }
}