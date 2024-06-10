package com.capstone.reseepe.ui.bookmarks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentBookmarksBinding
import com.capstone.reseepe.databinding.FragmentHomeBinding
import com.capstone.reseepe.ui.adapter.RecipeAdapter
import com.capstone.reseepe.ui.home.HomeViewModel

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarksViewModel =
            ViewModelProvider(this).get(BookmarksViewModel::class.java)

        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.rvRecipes.layoutManager = layoutManager

        // Populate RecyclerView with dummy data (replace with actual data)
        val dummyRecipeNames = listOf("Indonesian Fried Rice", "Chinese Fried Rice", "Kwetiauw", "Somay Mas Didit", "Mie Ayam Teguh") // Dummy recipe names
        val recipeAdapter = RecipeAdapter(dummyRecipeNames)
        binding.rvRecipes.adapter = recipeAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}