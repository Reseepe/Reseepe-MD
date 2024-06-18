package com.capstone.reseepe.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentResultBinding
import com.capstone.reseepe.databinding.FragmentScanBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.RecipeAdapter
import com.capstone.reseepe.ui.profile.ProfileViewModel
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.flexbox.FlexboxLayoutManager

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val resultViewModel by viewModels<ResultViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Set up RecyclerView with FlexboxLayoutManager
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        binding.rvIngredient.layoutManager = flexboxLayoutManager

        resultViewModel.ingredientList.observe(viewLifecycleOwner) {
            val ingredientAdapter= IngredientAdapter(it, enableHoldToDelete = true)
            binding.rvIngredient.adapter = ingredientAdapter
        }

        // Set up RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.rvRecipes.layoutManager = layoutManager

       resultViewModel.scanResultResponse.observe(viewLifecycleOwner){
           val recipeAdapter = RecipeAdapter(it.recommendedRecipes)
           binding.rvRecipes.adapter = recipeAdapter
       }

        resultViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }


        return root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}