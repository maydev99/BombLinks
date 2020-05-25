package com.bombadu.bomblinks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.bomblinks.db.LinkData
import com.squareup.picasso.Picasso

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    private var links: List<LinkData> = ArrayList()

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.link_card, parent,false)
        return MainHolder(itemView)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val currentLink = links[position]
        holder.textViewDate.text = currentLink.date
        holder.textViewDescription.text = currentLink.description
        holder.textViewSource.text = currentLink.source
        holder.textViewTitle.text = currentLink.title


        val imageUrl = currentLink.imageurl
        val myImageView = holder.imageViewImage
        Picasso.get().load(imageUrl).into(myImageView)

    }

    fun getMovieAt(position: Int) : LinkData? {
        return links[position]
    }

    fun setLinks(links: List<LinkData>) {
        this.links = links
        notifyDataSetChanged()
    }



    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var textViewDate: TextView = itemView.findViewById(R.id.card_date_text_view)
        var textViewTitle: TextView = itemView.findViewById(R.id.card_title_text_view)
        var textViewDescription: TextView = itemView.findViewById(R.id.card_description_text_view)
        var textViewSource: TextView = itemView.findViewById(R.id.card_source_text_view)
        var imageViewImage: ImageView = itemView.findViewById(R.id.card_imageView)
        override fun onClick(v: View?) {
            if (v != null) {
                onItemClick?.invoke(adapterPosition, v)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

    }


}