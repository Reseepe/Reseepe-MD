package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R

class RecipeAdapter(private val recipeNames: List<String>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipeName = recipeNames[position]
        holder.bind(recipeName)
    }

    override fun getItemCount() = recipeNames.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipeName: String) {
            itemView.findViewById<TextView>(R.id.tv_item_name).text = recipeName
        }
    }
}