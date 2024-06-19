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
import com.capstone.reseepe.R
import com.capstone.reseepe.data.dao.AppDatabase
import com.capstone.reseepe.data.dao.RecentlyViewedRecipe
import com.capstone.reseepe.data.response.RecommendedRecipesItem
import com.capstone.reseepe.databinding.FragmentDetailRecipeBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.InstructionAdapter
import com.capstone.reseepe.ui.home.HomeViewModel
import com.capstone.reseepe.ui.result.ResultViewModel
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.launch

class DetailRecipeFragment : Fragment() {

    private var _binding: FragmentDetailRecipeBinding? = null

    private lateinit var recipe: RecommendedRecipesItem

    private val binding get() = _binding!!

    private val ingredientListHave: MutableList<String> = mutableListOf()
    private val ingredientListMiss: MutableList<String> = mutableListOf()

    val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val detailViewModel by viewModels<DetailViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        recipe = arguments?.getParcelable("recipe") ?: throw IllegalArgumentException("Recipe not found")


        binding.floatingActionButton.setOnClickListener{
            if (recipe.isBookmarked == false){
                recipe.id?.let { it1 -> detailViewModel.postBookmark(it1) }
                setupFab(true)
            } else {
                recipe.id?.let { it1 -> detailViewModel.postUnbookmark(it1)}
                setupFab(false)
            }
        }

        recipe.isBookmarked?.let { setupFab(it) }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val flexboxLayoutManager1 = FlexboxLayoutManager(context)
        val flexboxLayoutManager2 = FlexboxLayoutManager(context)
        binding.rvIngredientHave.layoutManager = flexboxLayoutManager1
        binding.rvIngredientMiss.layoutManager = flexboxLayoutManager2

        binding.tvName.text = recipe.name
        binding.duration.text = recipe.duration.toString() + " Min"
        binding.ingredientsQuantity.text = recipe.ingredients?.size.toString() + " Ingredients"
        binding.tvDesc.text = recipe.description

        recipe.ingredients?.let { ingredientListHave.addAll(it.filterNotNull()) }
        recipe.missingIngredients?.let { ingredientListMiss.addAll(it.filterNotNull()) }

        val ingredientAdapterHave= IngredientAdapter(ingredientListHave, enableHoldToDelete = false)
        val ingredientAdapterMiss= IngredientAdapter(ingredientListMiss, enableHoldToDelete = false)

        binding.rvIngredientHave.adapter = ingredientAdapterHave
        binding.rvIngredientMiss.adapter = ingredientAdapterMiss

        val instructionList = recipe.instructions?.filterNotNull()
        val instructionAdapter = instructionList?.let { InstructionAdapter(it) }
        binding.rvInstructions.layoutManager = LinearLayoutManager(context)
        binding.rvInstructions.adapter = instructionAdapter

        saveRecentlyViewedRecipe(recipe)

        return root
    }

    private fun saveRecentlyViewedRecipe(recipe: RecommendedRecipesItem) {
        val recentlyViewedRecipe = RecentlyViewedRecipe(
            recipeId = recipe.id ?: 0,
            name = recipe.name ?: "",
            photoUrl = recipe.photoUrl,
            duration = recipe.duration ?: 0,
            ingredientsCount = recipe.ingredients?.size ?: 0,
            ingredients = recipe.ingredients?.filterNotNull() ?: emptyList(),
            instructions = recipe.instructions?.filterNotNull() ?: emptyList(),
            isBookmarked = recipe.isBookmarked,
            description = recipe.description,
            hasMissingIngredients = recipe.missingIngredients != null && recipe.missingIngredients.isNotEmpty(),
            missingIngredients = recipe.missingIngredients // Simpan missingIngredients
        )

        homeViewModel.saveRecentlyViewedRecipe(recentlyViewedRecipe)
    }


    private fun setupFab(isBookmarked: Boolean) {
        val iconResourceId = if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        binding.floatingActionButton.setImageResource(iconResourceId)

    }

}