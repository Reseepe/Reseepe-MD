package com.capstone.reseepe.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.reseepe.data.response.IngredientListItem
import com.capstone.reseepe.databinding.FragmentSearchIngredientsBinding
import com.capstone.reseepe.ui.adapter.IngredientAdapter
import com.capstone.reseepe.ui.adapter.SearchIngredientsAdapter
import com.capstone.reseepe.util.ViewModelFactory

class SearchIngredientsFragment : Fragment() {

    private var _binding: FragmentSearchIngredientsBinding? = null
    private val binding get() = _binding!!

    private val searchIngredientsViewModel by viewModels<SearchIngredientsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val resultViewModel: ResultViewModel by viewModels(
        ownerProducer = { requireActivity() }
    ) {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: SearchIngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)

        adapter = SearchIngredientsAdapter { ingredient ->
            showConfirmationDialog(ingredient)
        }
        binding.rvSearchResults.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            searchIngredientsViewModel.searchIngredients(query)
        }

        searchIngredientsViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            adapter.submitList(searchResults)
        }

        return root
    }

    private fun showConfirmationDialog(ingredient: IngredientListItem) {
        AlertDialog.Builder(requireContext())
            .setMessage("Add ${ingredient.name} to your list?")
            .setPositiveButton("Yes") { _, _ ->
                addIngredientToResultViewModel(ingredient.name)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun addIngredientToResultViewModel(ingredient: String) {
        resultViewModel.addIngredient(ingredient)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}