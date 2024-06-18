package com.capstone.reseepe.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.BookmarkedRecipesItem
import com.capstone.reseepe.data.response.RecommendedRecipesItem
import com.capstone.reseepe.databinding.FragmentDetailDefaultBinding
import com.capstone.reseepe.databinding.FragmentDetailRecipeBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.InstructionAdapter
import com.capstone.reseepe.ui.result.ResultViewModel
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.flexbox.FlexboxLayoutManager

class DetailDefaultFragment : Fragment() {

    private var _binding: FragmentDetailDefaultBinding? = null

    private lateinit var recipe: BookmarkedRecipesItem

    private val binding get() = _binding!!

    private val ingredientList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDefaultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val detailViewModel by viewModels<DetailViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        recipe = arguments?.getParcelable("recipe") ?: throw IllegalArgumentException("Recipe not found")

        recipe.isBookmarked?.let { setupFab(it) }

        binding.floatingActionButton.setOnClickListener{
            if (recipe.isBookmarked == false){
                recipe.id?.let { it1 -> detailViewModel.postBookmark(it1) }
            } else {
                recipe.id?.let { it1 -> detailViewModel.postUnbookmark(it1)}
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val flexboxLayoutManager1 = FlexboxLayoutManager(context)
        binding.rvIngredient.layoutManager = flexboxLayoutManager1

        binding.tvName.text = recipe.name
        binding.duration.text = recipe.duration.toString() + " Min"
        binding.ingredientsQuantity.text = recipe.ingredients?.size.toString() + " Ingredients"
        binding.tvDesc.text = recipe.description

        recipe.ingredients?.let { ingredientList.addAll(it.filterNotNull()) }


        val ingredientAdapterHave= IngredientAdapter(ingredientList, enableHoldToDelete = false)

        binding.rvIngredient.adapter = ingredientAdapterHave

        val instructionList = recipe.instructions?.filterNotNull()
        val instructionAdapter = instructionList?.let { InstructionAdapter(it) }
        binding.rvInstructions.layoutManager = LinearLayoutManager(context)
        binding.rvInstructions.adapter = instructionAdapter

        return root
    }

    private fun setupFab(isBookmarked: Boolean) {
        val iconResourceId = if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        binding.floatingActionButton.setImageResource(iconResourceId)

    }

}