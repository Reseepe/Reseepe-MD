package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R
import com.capstone.reseepe.data.model.RecipeItem

class RecentlyViewedAdapter(private val items: List<RecipeItem>, private val onItemClick: (RecipeItem) -> Unit) :
    RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImageView: ImageView = view.findViewById(R.id.iv_recipe_img)
        val titleTextView: TextView = view.findViewById(R.id.tv_recipe_title)
        val durationTextView: TextView = view.findViewById(R.id.duration)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredients_quantity)
        val forwardIcon: ImageView = view.findViewById(R.id.iv_forward_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recently, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.recipeImageView.setImageResource(item.imageRes)
        holder.titleTextView.text = item.title
        holder.durationTextView.text = item.duration
        holder.ingredientsTextView.text = item.ingredientsQuantity
        holder.forwardIcon.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
