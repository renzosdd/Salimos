package com.app.salimos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class ListEventsAdapter(private val list: List<Event>, private val clickListener: (Event) -> Unit)
    : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        (holder).bind(list[position],clickListener)
    }

    override fun getItemCount(): Int = list.size
}
