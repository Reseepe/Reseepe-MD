package com.capstone.reseepe.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentResultBinding
import com.capstone.reseepe.databinding.FragmentScanBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.RecipeAdapter
import com.google.android.flexbox.FlexboxLayoutManager

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView with FlexboxLayoutManager
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        binding.rvIngredient.layoutManager = flexboxLayoutManager

        // Example ingredient list
        val ingredientList = listOf("Tomato", "Cheese", "Lettuce", "Onion", "Bread", "Chicken", "Mayonnaise", "Mustard", "Ketchup")
        val ingredientAdapter= IngredientAdapter(ingredientList)
        binding.rvIngredient.adapter = ingredientAdapter

        // Set up RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.rvRecipes.layoutManager = layoutManager

        // Populate RecyclerView with dummy data (replace with actual data)
        val dummyRecipeNames = listOf("Indonesian Fried Rice", "Chinese Fried Rice", "Kwetiauw", "Somay Mas Didit") // Dummy recipe names
        val recipeAdapter = RecipeAdapter(dummyRecipeNames)
        binding.rvRecipes.adapter = recipeAdapter


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}