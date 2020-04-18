package com.app.salimos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var mNameView: TextView? = null
    private var mDateViewStart: TextView? = null
    private var mDateViewEnd: TextView? = null
    private var mImgView: ImageView? = null


    init {
        mNameView = itemView.findViewById(R.id.list_name)
        mDateViewStart = itemView.findViewById(R.id.list_date_start)
        mDateViewEnd = itemView.findViewById(R.id.list_date_end)
        mImgView = itemView.findViewById(R.id.list_img) as ImageView
    }

    fun bind(event: Event, clickListener: (Event) -> Unit) {
        mNameView?.text = event.name
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        mDateViewStart?.text =  "Desde: " + LocalDateTime.parse(event.date_start).format(formatter).toString()
        mDateViewEnd?.text =  "Hasta: " + LocalDateTime.parse(event.date_end).format(formatter).toString()
        mImgView?.loadUrl(event.image)
        itemView.setOnClickListener { clickListener(event) }
    }

    private fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }

}