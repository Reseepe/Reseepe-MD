package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R

class IngredientAdapter(private val ingredientList: List<String>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredientList[position]
        holder.textView.text = currentItem
    }

    override fun getItemCount() = ingredientList.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_item_name)
    }
}