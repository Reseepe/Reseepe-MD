package com.capstone.reseepe.ui.bookmarks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentBookmarksBinding
import com.capstone.reseepe.databinding.FragmentHomeBinding
import com.capstone.reseepe.ui.adapter.RecipeAdapter
import com.capstone.reseepe.ui.home.HomeViewModel
import com.capstone.reseepe.ui.result.ResultViewModel
import com.capstone.reseepe.util.ViewModelFactory

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarksViewModel by viewModels<BookmarksViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.rvRecipes.layoutManager = layoutManager

        bookmarksViewModel.getBookmarkResponse.observe(viewLifecycleOwner){
            val recipeAdapter = it.bookmarkedRecipes?.let { it1 -> RecipeAdapter(it1.filterNotNull()) }
            binding.rvRecipes.adapter = recipeAdapter
        }

        bookmarksViewModel.isEmpty.observe(viewLifecycleOwner){
            showInfo(it)
        }

        return root
    }

    private fun showInfo(isEmpty: Boolean) {
        binding.ivNoBookmarks.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tvNoBookmarks.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}