package com.capstone.reseepe.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.reseepe.R
import com.capstone.reseepe.data.dao.AppDatabase
import com.capstone.reseepe.data.dao.RecentlyViewedRecipe
import com.capstone.reseepe.data.response.BookmarkedRecipesItem
import com.capstone.reseepe.data.response.RecommendedListItem
import com.capstone.reseepe.databinding.FragmentDetailDefaultBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.InstructionAdapter
import com.capstone.reseepe.ui.home.HomeViewModel
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch

class DetailDefaultFragment : Fragment() {

    private var _binding: FragmentDetailDefaultBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipe: Any
    private val ingredientList: MutableList<String> = mutableListOf()

    val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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
                    setupFab(true)
                } else {
                    bookmarkedRecipe.id?.let { id -> detailViewModel.postUnbookmark(id) }
                    setupFab(false)
                }
            }

            binding.tvName.text = bookmarkedRecipe.name
            binding.duration.text = "${bookmarkedRecipe.duration} Min"
            binding.ingredientsQuantity.text = "${bookmarkedRecipe.ingredients?.size} Ingredients"
            binding.tvDesc.text = bookmarkedRecipe.description

            bookmarkedRecipe.ingredients?.let { ingredientList.addAll(it.filterNotNull()) }
            val instructionList = bookmarkedRecipe.instructions?.filterNotNull()
            setupRecyclerViews(ingredientList, instructionList)

            saveRecentlyViewedRecipe(bookmarkedRecipe)

        } else if (recipe is RecommendedListItem) {
            val recommendedRecipe = recipe as RecommendedListItem

            recommendedRecipe.isBookmarked?.let { setupFab(it) }

            binding.floatingActionButton.setOnClickListener {
                if (recommendedRecipe.isBookmarked == false) {
                    recommendedRecipe.id?.let { id -> detailViewModel.postBookmark(id) }
                    setupFab(true)
                } else {
                    recommendedRecipe.id?.let { id -> detailViewModel.postUnbookmark(id) }
                    setupFab(false)
                }
            }

            binding.tvName.text = recommendedRecipe.name
            binding.duration.text = "${recommendedRecipe.duration} Min"
            binding.ingredientsQuantity.text = "${recommendedRecipe.ingredients.size} Ingredients"
            binding.tvDesc.text = recommendedRecipe.description

            ingredientList.clear()
            ingredientList.addAll(recommendedRecipe.ingredients.filterNotNull())
            val instructionList = recommendedRecipe.instructions?.filterNotNull()
            setupRecyclerViews(ingredientList, instructionList)

            saveRecentlyViewedRecipe(recommendedRecipe)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return root
    }

    private fun saveRecentlyViewedRecipe(recipe: Any) {
        val recentlyViewedRecipe = when (recipe) {
            is BookmarkedRecipesItem -> RecentlyViewedRecipe(
                recipeId = recipe.id ?: 0,
                name = recipe.name ?: "",
                photoUrl = recipe.photoUrl,
                duration = recipe.duration ?: 0,
                ingredientsCount = recipe.ingredients?.size ?: 0,
                ingredients = recipe.ingredients?.filterNotNull() ?: emptyList(),
                instructions = recipe.instructions?.filterNotNull() ?: emptyList(),
                isBookmarked = recipe.isBookmarked,
                description = recipe.description,
                hasMissingIngredients = false,
                missingIngredients = null // Tidak ada missingIngredients di sini
            )
            is RecommendedListItem -> RecentlyViewedRecipe(
                recipeId = recipe.id,
                name = recipe.name,
                photoUrl = recipe.photoUrl,
                duration = recipe.duration,
                ingredientsCount = recipe.ingredients.size,
                ingredients = recipe.ingredients,
                instructions = recipe.instructions,
                isBookmarked = recipe.isBookmarked,
                description = recipe.description,
                hasMissingIngredients = false,
                missingIngredients = null // Tidak ada missingIngredients di sini
            )
            else -> return
        }

        homeViewModel.saveRecentlyViewedRecipe(recentlyViewedRecipe)

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
