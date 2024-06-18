package com.capstone.reseepe.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.RecommendedListItem

class CarouselAdapter(
    private val recommendedItems: List<RecommendedListItem>,
    private val onItemClick: (RecommendedListItem) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivRecipe: ImageView = view.findViewById(R.id.iv_rec_img)
        val tvRecipeName: TextView = view.findViewById(R.id.recipe_name_car)
        val tvDuration: TextView = view.findViewById(R.id.duration_car)
        val tvIngredients: TextView = view.findViewById(R.id.ingredients_quantity_car)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val recommendedItem = recommendedItems[position]

        holder.ivRecipe.setImageResource(R.drawable.default_val_rcp)
        holder.tvRecipeName.text = recommendedItem.name
        holder.tvDuration.text = "${recommendedItem.duration} mins"
        holder.tvIngredients.text = "${recommendedItem.ingredients.size} ingredients"

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply{
                putParcelable("recipe", recommendedItem)
            }
            it.findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment)
        }
    }

    override fun getItemCount(): Int {
        return recommendedItems.size
    }
}

