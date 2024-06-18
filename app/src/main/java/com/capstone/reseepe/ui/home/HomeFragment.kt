package com.capstone.reseepe.ui.home

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.reseepe.R
import com.capstone.reseepe.data.model.RecipeItem
import com.capstone.reseepe.databinding.FragmentHomeBinding
import com.capstone.reseepe.ui.adapter.CarouselAdapter
import com.capstone.reseepe.ui.adapter.RecentlyViewedAdapter
import com.capstone.reseepe.ui.profile.ProfileViewModel
import com.capstone.reseepe.util.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel by viewModels<HomeViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        val profileViewModel by viewModels<ProfileViewModel> {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        ////      Show loading indicator
//        binding.progressBar.visibility = View.VISIBLE
//        binding.homeContainer.visibility = View.GONE

        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer { profileResponse ->
//            // Hide loading indicator
//            binding.progressBar.visibility = View.GONE
//            binding.homeContainer.visibility = View.VISIBLE

            profileResponse?.let {
                val name = it.name
                binding.tvTitleMsg.text = "Hello, $name"
            }
        })

        profileViewModel.fetchProfile()

        // Inisialisasi ViewPager2 dan Adapter
        val viewPager: ViewPager2 = binding.viewPager
        val recipeItems = listOf(
            RecipeItem(R.drawable.default_val_rcp, "Recipe 1", "30 mins", "10 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 2", "45 mins", "8 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 3", "20 mins", "5 ingredients")
        )
        val adapter = CarouselAdapter(recipeItems) { recipeItem ->
            val bundle = Bundle().apply {
                putString("recipeName", recipeItem.title)
            }
            findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment, bundle)
        }
        viewPager.adapter = adapter

        // Slide otomatis
        handler = Handler()
        runnable = Runnable {
            viewPager.currentItem = (viewPager.currentItem + 1) % recipeItems.size
        }
        startAutoScroll()

        // Menunda otomatis geser ketika user menyentuh ViewPager2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    stopAutoScroll()
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    startAutoScroll()
                }
            }
        })

        // Inisialisasi RecyclerView dan Adapter untuk Recently Viewed
        val rvRecently = binding.rvRecently
        val recipeItemsRecently = listOf(
            RecipeItem(R.drawable.default_val_rcp, "Recipe 1", "30 mins", "10 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 2", "45 mins", "8 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 3", "20 mins", "5 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 4", "20 mins", "5 ingredients"),
            RecipeItem(R.drawable.default_val_rcp, "Recipe 5", "20 mins", "5 ingredients"),
        )
        val recentlyViewedAdapter = RecentlyViewedAdapter(recipeItemsRecently) { item ->
            val bundle = Bundle().apply {
                putString("recipeName", item.title)
            }
            findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment, bundle)
        }
        rvRecently.adapter = recentlyViewedAdapter
        rvRecently.layoutManager = LinearLayoutManager(context)

        return root
    }

    private fun startAutoScroll() {
        handler.postDelayed(runnable, 3000)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
