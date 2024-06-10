package com.capstone.reseepe.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentBookmarksBinding
import com.capstone.reseepe.databinding.FragmentDetailRecipeBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.InstructionAdapter
import com.google.android.flexbox.FlexboxLayoutManager

class DetailRecipeFragment : Fragment() {

    private var _binding: FragmentDetailRecipeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val flexboxLayoutManager1 = FlexboxLayoutManager(context)
        val flexboxLayoutManager2 = FlexboxLayoutManager(context)
        binding.rvIngredientHave.layoutManager = flexboxLayoutManager1
        binding.rvIngredientMiss.layoutManager = flexboxLayoutManager2

        val recipeName = arguments?.getString("recipeName")
        binding.tvName.text = recipeName

        val ingredientListHave = listOf("Tomato", "Cheese", "Lettuce", "Onion", "Bread", "Chicken", "Mayonnaise", "Mustard", "Ketchup")
        val ingredientListMiss = listOf("Asparagus", "Rice", "Soy Sauce", "Chili", "MSG", )

        val ingredientAdapterHave= IngredientAdapter(ingredientListHave)
        val ingredientAdapterMiss= IngredientAdapter(ingredientListMiss)

        binding.rvIngredientHave.adapter = ingredientAdapterHave
        binding.rvIngredientMiss.adapter = ingredientAdapterMiss

        val instructionList = listOf(
            "Clean the ingredients",
            "Turn on the stove",
            "COOOOOOOOOOK",
            "Plate it nicely",
            "ENJOY MENNN"
        )

        val instructionAdapter = InstructionAdapter(instructionList)
        binding.rvInstructions.layoutManager = LinearLayoutManager(context)
        binding.rvInstructions.adapter = instructionAdapter

        return root
    }

}