package com.capstone.reseepe.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.reseepe.R

class InstructionAdapter(private val instructionList: List<String>) : RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_instructions, parent, false)
        return InstructionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val stepNumber = position + 1
        val instruction = instructionList[position]

        holder.stepNumberTextView.text = stepNumber.toString()
        holder.stepDescriptionTextView.text = instruction
    }

    override fun getItemCount(): Int {
        return instructionList.size
    }

    class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepNumberTextView: TextView = itemView.findViewById(R.id.tv_step_number)
        val stepDescriptionTextView: TextView = itemView.findViewById(R.id.tv_step_description)
    }
}
