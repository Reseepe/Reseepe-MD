package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.IngredientListItem
import com.capstone.reseepe.databinding.ItemIngredientSearchBinding
import android.view.View
import android.widget.TextView

class SearchIngredientsAdapter(
    private val onItemClick: (IngredientListItem) -> Unit
) : ListAdapter<IngredientListItem, SearchIngredientsAdapter.IngredientViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_search, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bind(ingredient, onItemClick)
    }

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingredientNameTextView: TextView = itemView.findViewById(R.id.tv_ingredient_name)

        fun bind(ingredient: IngredientListItem, onItemClick: (IngredientListItem) -> Unit) {
            ingredientNameTextView.text = ingredient.name
            itemView.setOnClickListener {
                onItemClick(ingredient)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<IngredientListItem>() {
        override fun areItemsTheSame(oldItem: IngredientListItem, newItem: IngredientListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IngredientListItem, newItem: IngredientListItem): Boolean {
            return oldItem == newItem
        }
    }
}