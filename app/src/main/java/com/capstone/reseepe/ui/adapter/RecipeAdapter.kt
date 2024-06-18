package com.capstone.reseepe.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.BookmarkedRecipesItem
import com.capstone.reseepe.data.response.RecommendedRecipesItem

class RecipeAdapter<T>(private val recipes: List<T>) :
    RecyclerView.Adapter<RecipeAdapter<T>.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                when (recipe) {
                    is RecommendedRecipesItem -> putParcelable("recipe", recipe)
                    is BookmarkedRecipesItem -> putParcelable("recipe", recipe)
                }
            }
            it.findNavController().navigate(R.id.action_resultFragment_to_detailRecipeFragment, bundle)
        }
    }

    override fun getItemCount() = recipes.size

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: T) {
            when (recipe) {
                is RecommendedRecipesItem -> {
                    itemView.findViewById<TextView>(R.id.tv_item_name).text = recipe.name
                }
                is BookmarkedRecipesItem -> {
                    itemView.findViewById<TextView>(R.id.tv_item_name).text = recipe.name
                }
            }
        }
    }
}