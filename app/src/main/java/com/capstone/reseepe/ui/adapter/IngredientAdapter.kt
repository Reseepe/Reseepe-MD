package com.capstone.reseepe.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R

class IngredientAdapter(
    private val ingredientList: MutableList<String>,
    private val enableHoldToDelete: Boolean = false,
    private val onDeleteIngredient: (String) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredientList[position]
        holder.textView.text = currentItem

        if (enableHoldToDelete) {
            holder.itemView.setOnLongClickListener {
                showCustomDialog(holder.itemView.context, currentItem, position)
                true
            }
        } else {
            holder.itemView.setOnLongClickListener(null)
        }
    }

    override fun getItemCount() = ingredientList.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    private fun showCustomDialog(context: Context, ingredient: String, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation_delete, null)

        val dialogMessage = dialogView.findViewById<TextView>(R.id.tv_msg)
        val dialogDelete = dialogView.findViewById<Button>(R.id.btn_delete)
        val dialogCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        dialogMessage.text = "Are you sure you want to delete '$ingredient'?"

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogDelete.setOnClickListener {
            deleteIngredient(position)
            dialog.dismiss()
        }

        dialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteIngredient(position: Int) {
        if (position in 0 until ingredientList.size) {
            val removedIngredient = ingredientList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, ingredientList.size)
            onDeleteIngredient(removedIngredient)
        }
    }

}
