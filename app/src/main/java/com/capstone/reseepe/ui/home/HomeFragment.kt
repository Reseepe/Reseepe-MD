package com.capstone.reseepe.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.reseepe.R
import com.capstone.reseepe.data.response.RecommendedListItem
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

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var recentlyViewedAdapter: RecentlyViewedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObservers()

        // Fetch user profile data
        profileViewModel.userProfile.observe(viewLifecycleOwner, Observer { profileResponse ->
            profileResponse?.let {
                val name = it.name
                binding.tvTitleMsg.text = "Hello, $name"
            }
        })
        profileViewModel.fetchProfile()

        return root
    }

    private fun setupObservers() {
        // Observe recently viewed recipes
        // Mengamati LiveData menggunakan observe
        homeViewModel.recentlyViewedRecipes?.observe(viewLifecycleOwner, Observer { recipes ->
            recipes?.let { // Memastikan recipes tidak null
                val list = it // Mengakses value dari LiveData
                list?.let { // Memastikan list tidak null
                    recentlyViewedAdapter.submitList(it.filterNotNull())
                }
            }
        })
        

        // Observe top five recommendations
        homeViewModel.topFiveRecommendations.observe(viewLifecycleOwner, Observer { recommendations ->
            recommendations?.let { recommendedList ->
                setupCarousel(recommendedList)
            }
        })

        // Fetch top five recommendations
        homeViewModel.fetchTopFiveRecommendations()

        // Setup RecyclerView for recently viewed recipes
        setupRecentlyViewedRecyclerView()
    }

    private fun setupCarousel(recommendedList: List<RecommendedListItem>) {
        val viewPager: ViewPager2 = binding.viewPager
        val adapter = CarouselAdapter(recommendedList) { recommendedItem ->
            val bundle = Bundle().apply {
                putInt("recipeId", recommendedItem.id)
            }
            findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment, bundle)
        }
        viewPager.adapter = adapter

        handler = Handler()
        runnable = Runnable {
            viewPager.currentItem = (viewPager.currentItem + 1) % recommendedList.size
        }
        startAutoScroll()

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
    }

    private fun setupRecentlyViewedRecyclerView() {
        val rvRecently = binding.rvRecently
        recentlyViewedAdapter = RecentlyViewedAdapter { item ->
            val bundle = Bundle().apply {
                putString("recipeName", item.name)
            }
            findNavController().navigate(R.id.action_navigation_home_to_detailRecipeFragment, bundle)
        }
        rvRecently.adapter = recentlyViewedAdapter
        rvRecently.layoutManager = LinearLayoutManager(context)
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
