package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R
import com.capstone.reseepe.data.model.RecipeItem

class CarouselAdapter(
    private val recipeItems: List<RecipeItem>,
    private val onItemClick: (RecipeItem) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_recipe_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val recipeItem = recipeItems[position]
        holder.imageView.setImageResource(recipeItem.imageRes)
        holder.imageView.setOnClickListener {
            onItemClick(recipeItem)
        }
    }

    override fun getItemCount(): Int {
        return recipeItems.size
    }
}
