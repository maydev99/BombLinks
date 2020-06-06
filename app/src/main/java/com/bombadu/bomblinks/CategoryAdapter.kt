package com.bombadu.bomblinks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.bomblinks.db.CategoryMinimal

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    private var categories: List<CategoryMinimal> = ArrayList()
    private var itemClickCallback: ItemClickCallback? = null
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null


    internal interface  ItemClickCallback {
        fun onItemClick(p: Int)
    }

    internal fun setItemClickCallback (inItemClickCallback: ItemClickCallback) {
        this.itemClickCallback = inItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun getCategoryAt(position: Int): CategoryMinimal? {
        return categories[position]
    }

    fun setCategories(myCategory: List<CategoryMinimal>) {
        this.categories = myCategory
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val currentCategory = categories[position]
        holder.textViewCategory.text = currentCategory.category
    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewCategory: TextView = itemView.findViewById(R.id.category_item_text_view)
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