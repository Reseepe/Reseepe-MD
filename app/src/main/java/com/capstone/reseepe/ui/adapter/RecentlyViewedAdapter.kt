package com.capstone.reseepe.ui.adapter

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R
import com.capstone.reseepe.data.dao.RecentlyViewedRecipe
import com.capstone.reseepe.data.response.BookmarkedRecipesItem
import com.capstone.reseepe.data.response.RecommendedListItem
import com.capstone.reseepe.data.response.RecommendedRecipesItem
import com.capstone.reseepe.databinding.ItemRecentlyBinding

class RecentlyViewedAdapter(
    private val onItemClick: (RecentlyViewedRecipe) -> Unit
) : ListAdapter<RecentlyViewedRecipe, RecentlyViewedAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecentlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: ItemRecentlyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecentlyViewedRecipe) {
            binding.tvRecipeTitle.text = recipe.name
            binding.ivRecipeImg.setImageResource(R.drawable.default_val_rcp)
            binding.duration.text = "${recipe.duration} mins"
            binding.ingredientsQuantity.text = "${recipe.ingredientsCount} ingredients"

            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("recipe", convertToRecipeItem(recipe))
                }

                if (recipe.hasMissingIngredients) {
                    it.findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment, bundle)
                } else {
                    it.findNavController().navigate(R.id.action_navigation_home_to_navigation_detail_default, bundle)
                }
            }
        }
    }

    private fun convertToRecipeItem(recipe: RecentlyViewedRecipe): Parcelable {
        return if (recipe.isBookmarked == true) {
            BookmarkedRecipesItem(
                id = recipe.recipeId,
                name = recipe.name,
                photoUrl = recipe.photoUrl,
                duration = recipe.duration,
                ingredients = recipe.ingredients,
                instructions = recipe.instructions,
                description = recipe.description,
                isBookmarked = true
            )
        } else if (recipe.hasMissingIngredients) {
            RecommendedRecipesItem(
                id = recipe.recipeId,
                name = recipe.name,
                photoUrl = recipe.photoUrl,
                duration = recipe.duration,
                ingredients = recipe.ingredients,
                instructions = recipe.instructions,
                description = recipe.description,
                isBookmarked = false,
                missingIngredients = recipe.missingIngredients
            )
        } else {
            RecommendedListItem(
                id = recipe.recipeId,
                name = recipe.name,
                photoUrl = recipe.photoUrl,
                duration = recipe.duration,
                ingredients = recipe.ingredients,
                instructions = recipe.instructions,
                description = recipe.description,
                isBookmarked = false
            )
        }
    }



    class RecipeDiffCallback : DiffUtil.ItemCallback<RecentlyViewedRecipe>() {
        override fun areItemsTheSame(oldItem: RecentlyViewedRecipe, newItem: RecentlyViewedRecipe): Boolean {
            return oldItem.id == newItem.id // Assuming id is unique
        }

        override fun areContentsTheSame(oldItem: RecentlyViewedRecipe, newItem: RecentlyViewedRecipe): Boolean {
            return oldItem == newItem // Assuming equals is overridden correctly
        }
    }
}
