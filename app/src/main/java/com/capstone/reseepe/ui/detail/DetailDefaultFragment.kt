package com.capstone.reseepe.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.BookmarkedRecipesItem
import com.capstone.reseepe.data.response.RecommendedListItem
import com.capstone.reseepe.databinding.FragmentDetailDefaultBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.InstructionAdapter
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.flexbox.FlexboxLayoutManager

class DetailDefaultFragment : Fragment() {

    private var _binding: FragmentDetailDefaultBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipe: Any
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

        // Get the recipe object from arguments
        recipe = arguments?.getParcelable("recipe") ?: throw IllegalArgumentException("Recipe not found")

        if (recipe is BookmarkedRecipesItem) {
            val bookmarkedRecipe = recipe as BookmarkedRecipesItem
            bookmarkedRecipe.isBookmarked?.let { setupFab(it) }

            binding.floatingActionButton.setOnClickListener {
                if (bookmarkedRecipe.isBookmarked == false) {
                    bookmarkedRecipe.id?.let { id -> detailViewModel.postBookmark(id) }
                } else {
                    bookmarkedRecipe.id?.let { id -> detailViewModel.postUnbookmark(id) }
                }
            }

            binding.tvName.text = bookmarkedRecipe.name
            binding.duration.text = "${bookmarkedRecipe.duration} Min"
            binding.ingredientsQuantity.text = "${bookmarkedRecipe.ingredients?.size} Ingredients"
            binding.tvDesc.text = bookmarkedRecipe.description

            bookmarkedRecipe.ingredients?.let { ingredientList.addAll(it.filterNotNull()) }
            val instructionList = bookmarkedRecipe.instructions?.filterNotNull()
            setupRecyclerViews(ingredientList, instructionList)

        } else if (recipe is RecommendedListItem) {
            val recommendedRecipe = recipe as RecommendedListItem

            binding.floatingActionButton.visibility = View.GONE

            binding.tvName.text = recommendedRecipe.name
            binding.duration.text = "${recommendedRecipe.duration} Min"
            binding.ingredientsQuantity.text = "${recommendedRecipe.ingredients.size} Ingredients"
            binding.tvDesc.text = recommendedRecipe.description

            ingredientList.clear()
            ingredientList.addAll(recommendedRecipe.ingredients.filterNotNull())
            val instructionList = recommendedRecipe.instructions?.filterNotNull()
            setupRecyclerViews(ingredientList, instructionList)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return root
    }

    private fun setupFab(isBookmarked: Boolean) {
        val iconResourceId = if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        binding.floatingActionButton.setImageResource(iconResourceId)
    }

    private fun setupRecyclerViews(ingredients: MutableList<String>, instructions: List<String>?) {
        val flexboxLayoutManager1 = FlexboxLayoutManager(context)
        binding.rvIngredient.layoutManager = flexboxLayoutManager1
        val ingredientAdapter = IngredientAdapter(ingredients, enableHoldToDelete = false)
        binding.rvIngredient.adapter = ingredientAdapter

        binding.rvInstructions.layoutManager = LinearLayoutManager(context)
        val instructionAdapter = InstructionAdapter(instructions ?: emptyList())
        binding.rvInstructions.adapter = instructionAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
