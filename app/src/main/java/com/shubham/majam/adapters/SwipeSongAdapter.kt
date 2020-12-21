package com.shubham.majam.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.shubham.majam.R
import kotlinx.android.synthetic.main.list_item.view.*

class SwipeSongAdapter : BaseSongAdapter(R.layout.list_item) {
    override val differ = AsyncListDiffer(this, diffCallBack)

    override fun onBindViewHolder(holder: BaseSongAdapter.SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            val text = "${song.title} - ${song.subtitle}"
            tvPrimary.text = text

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}